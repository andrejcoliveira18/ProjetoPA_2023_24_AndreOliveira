/**
 * XML Entity.
 *
 * You can use it to generate XML Entities
 *
 * @param name Entity's name
 * @param text Entity's associated text.
 * @param father Entity's father entity
 * @param attributes Attributes List
 * @param children Entity's children
 * @constructor Create a new Entity.
 */
data class Entity (
    private var name: String,
    private var text: String? = "",
    private var father: Entity? = null,
    private var attributes: MutableList<Attribute> = mutableListOf(),
    private var children: MutableList<Entity> = mutableListOf()

) {
    init{
        require(name.isNotBlank()) { "The name must not be blank." }
        require(!name.contains(" ")) {"The entity must not contain blank spaces." }
        require(!name.contains(Regex("[^a-zA-Z0-9]"))) { "The name cannot contain special characters" }
        /**
         * If an entity is created with an instantiated father, it's added to father's children
         */
        father?.addChild(this)
    }

    /**
     * @return Entity's name
     */
    fun getName(): String = name

    /**
     * @return Entity's text
     */
    fun getText(): String? = text

    /**
     * @return Entity's father
     */
    fun getFather(): Entity? = father

    /**
     * @return Entity's attribute list
     */
    fun getAttributes(): MutableList<Attribute> = attributes

    /**
     * @return Entity's children list
     */
    fun getChildren(): MutableList<Entity> = children

    /**
     * Set the Entity's name
     * @param newName New entity's name
     */
    fun setName(newName: String){
        name = newName
    }

    /**
     * Sets the Entity's text
     * @param newText New text
     */
    fun setText(newText: String){
        text = newText
    }

    /**
     * Sets a new father to entity
     * @param newFather New Father
     */
    fun setFather(newFather: Entity){
        father?.deleteChild(this)
        father = newFather
    }

    /**
     * Sets a new attributes list
     * @param newAttributes New Attributes List
     * */
    fun setAttributes(newAttributes: MutableList<Attribute>){
        attributes = newAttributes
    }

    /**
     * Sets a new Children list
     * @param newChildren New children list
     */
    fun setChildren(newChildren: MutableList<Entity>){
        children = newChildren
    }

    /**
     * Visitor.
     * @param visitor
     */
    fun accept(visitor: (Entity) -> Boolean){
        if(visitor(this))
            children.forEach {
                it.accept(visitor)
            }
    }

    /**
     * Add new child to Entity
     * @param child New child
     */
    fun addChild(child: Entity){
        children.add(child)
    }

    /**
     * Add new attribute to Entity
     * @param att New attribute
     */
    fun addAttribute(att: Attribute){
        attributes.add(att)
    }

    /**
     * Remove attribute
     * @param att Attribute to remove
     */
    fun removeAttribute(att: Attribute){
        attributes.remove(att)
    }

    /**
     * Remove entity child
     * @param child Child to remove
     */
    fun deleteChild(child: Entity) {
        children.remove(child)
    }

    /**
     * Translate  XML Entity to XML formatted string
     * @param tabCount The default value is 0 and the purpose is to control XML tabulation.
     */
    fun prettyPrint(tabCount: Int = 0): String {
        var result = "\t".repeat(tabCount) + "<${name}"
        attributes.forEach { attribute ->
            result += " ${attribute.getName()}=\"${attribute.getValue()}\""
        }
        if (children.isEmpty() && text.isNullOrEmpty()) {
            if (attributes.isEmpty()) {
                result += "></$name>\n"
            } else {
                result += "/>\n"
            }
        } else {
            result += ">"
            if (!text.isNullOrEmpty()) {
                result += text
            }
            if (children.isEmpty()){
                result += "</${name}>\n"
            } else {
                result += "\n"
                children.forEach { child ->
                    result += child.prettyPrint(tabCount + 1)
                }
                result += "\t".repeat(tabCount) + "</${name}>\n"
            }
        }
        return result
    }
}