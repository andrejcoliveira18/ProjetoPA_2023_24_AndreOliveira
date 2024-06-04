/**
 * XML Entity Attribute Class
 *
 * @param name Attribute's name
 * @param value Attribute's value
 */
data class Attribute (
    private var name: String,
    private var value: String
) {
    init {
        /**
         * Requirements to attributes creation
         */
        require(name.isNotBlank()) { "The Attribute name must not be blank" }
        require(!name.contains(" ")) {"The Attribute name must not contain blank spaces"}
        require(!name.contains(Regex("[^a-zA-Z0-9]"))) { "The Attribute name must not contain special characters" }
    }

    /**
     * @return Attribute name
     */
    fun getName(): String = name

    /**
     * @return Attribute value
     */
    fun getValue(): String = value

    /**
     * Sets Attribute new name
     * @param newName New name
     */
    fun setName(newName: String){
        name = newName
    }

    /**
     * Sets Attribute new value
     * @param newValue New value
     */
    fun setValue(newValue: String){
        value = newValue
    }
}