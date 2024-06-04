import java.io.File

/**
 * XML Document
 * @param name Document Name
 * @param root Document Root Entity
 */
data class Document (
    private var name:String,
    private var root: Entity
){

    /**
     * @return Document Root
     */
    fun getRoot(): Entity = root

    /**
     * @return Document Name
     */
    fun getName(): String = name

    /**
     * Sets the Document Name
     * @param newName Document's new name
     */
    fun setName(newName: String){
        name = newName
    }

    /**
     * Sets the Document root
     * @param newRoot Document's new root
     */
    fun setRoot(newRoot: Entity){
        root = newRoot
    }

    /**
     * Adds an attribute to a Document's Entity
     * @param entityName Entity name to add attribute
     * @param attName Attribute's name
     * @param attValue Attribute's value
     */
    fun addAttribute(entityName: String, attName: String, attValue: String){
        root.accept {
            if(it.getName() == entityName){
                it.addAttribute(Attribute(attName, attValue))
            }
            true
        }
    }

    /**
     * Changes a Document Entity name
     * @param oldName Entity's old name
     * @param newName Entity's new name
     */
    fun renameEntity(oldName: String, newName: String){
        root.accept {
            if(it.getName() == oldName){
                it.setName(newName)
            }
            true
        }
    }


    /**
     * Changes an attribute name in a given entity name
     * @param entityName Entity's name
     * @param oldAttName Old Attribute's name
     * @param newAttName New Attribute's name
     */
    fun renameAttribute(entityName: String, oldAttName: String, newAttName: String){
        root.accept {
            if(it.getName() == entityName){
                it.getAttributes().forEach { a ->
                    if(a.getName() == oldAttName){
                        a.setName(newAttName)
                    }
                }
            }
            true
        }
    }

    /**
     * Removes all entities with a given name
     * @param entityName Entitie(s) to remove name
     */
    fun removeEntity(entityName: String) {
        val entitiesToRemove = mutableListOf<Entity>()
        root.accept { entity ->
            if (entity.getName() == entityName) {
                entitiesToRemove.add(entity)
            }
            true
        }

        entitiesToRemove.forEach { entity ->
            entity.getFather()?.deleteChild(entity)
        }
    }

    /**
     * Removes all given Attribute's name in a given Entity's name
     * @param entityName Entity's name
     * @param attName Attribute's name
     */
    fun removeAttribute(entityName: String, attName: String) {
        root.accept { entity ->
            if (entity.getName() == entityName) {
                entity.getAttributes().removeIf { attribute -> attribute.getName() == attName }
            }
            true
        }
    }

    /**
     * Returns a XML equivalent string
     * @return String in XML
     */
    fun prettyPrint(): String{
        val header = """<?xml version="1.0" encoding="UTF-8"?>""" + "\n"
        return header + root.prettyPrint()
    }

    /**
     * PrettyPrint result to file
     * @return File object
     */
    fun prettyPrintToFile(outName: String): File {
        val f = File(outName)
        f.writeText(prettyPrint())
        return f
    }

    /**
     * Given a Micro xPath, returns a list of correspondent entities
     * @param xPath XPath
     * @return Entity list
     */
    fun microXPath(xPath: String): MutableList<Entity> {
        val entities = xPath.split('/')
        var currentLevel = root.getChildren()
        for (entity in entities) {
            val listFound = currentLevel.filter { f -> f.getName() == entity }
            if (entity == entities[entities.lastIndex]) {
                return (currentLevel.filter { e -> e.getName() == entity }).toMutableList()
            } else {
                currentLevel.clear()
                for (found in listFound) {
                    found.getChildren().forEach { c -> currentLevel.add(c) }
                }
            }
        }
        return currentLevel
    }
}