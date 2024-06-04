import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

@Target(AnnotationTarget.PROPERTY)
annotation class XmlAttribute

@Target(AnnotationTarget.CLASS)
annotation class XmlAdapter(val f: KClass<out EntityAdapter>)

@Target(AnnotationTarget.PROPERTY)
annotation class XmlString(val f: KClass<out AttributeAdapter<*>>)

/**
 * Interface that allow us to create an adapter for attribute
 */
interface AttributeAdapter<Any> {
    fun adapt(att: Any): String
}

/**
 * Interface that allow us to create an adapter for an entity
 */
interface EntityAdapter {
    fun adapt(ent: Entity)
}

/**
 * List data class fields sorted
 */
val KClass<*>.dataClassFields: List<KProperty<*>>
    get() {
        require(isData) { "instance must be data class" }
        return primaryConstructor!!.parameters.map { p ->
            declaredMemberProperties.find { it.name == p.name }!!
        }
    }

/**
 * Translates any Object to XML Entity
 * @param obj Object to convert
 * @return Resultant Entity
 */
fun translate(obj: Any): Entity {
    val main = Entity(obj::class.simpleName!!)
    val clazz = obj::class
    clazz.dataClassFields.forEach {
        var value = it.call(obj)
        val xmlStringAnnotation = it.findAnnotation<XmlString>()
        if (xmlStringAnnotation != null) {
            val adapterInstance = xmlStringAnnotation.f.createInstance()
            value = (adapterInstance as AttributeAdapter<Any>).adapt(value!!)
        }
        if (value !is List<*>) {
            if (it.findAnnotation<XmlAttribute>() != null) {
                val newAttribute = Attribute(it.name, value.toString())
                main.addAttribute(newAttribute)
            } else {
                val newEntity = Entity(it.name)
                main.addChild(newEntity)
            }
        } else {
            val newEntity = Entity(it.name)
            value.forEach { e ->
                newEntity.addChild(translate(e!!))
            }
            main.addChild(newEntity)
        }
    }
    val xmlAdapterAnnotation = obj::class.findAnnotation<XmlAdapter>()
    if (xmlAdapterAnnotation != null){
        val adapterInstance = xmlAdapterAnnotation.f.createInstance()
        (adapterInstance as EntityAdapter).adapt(main)
    }
    return main
}

/**
 * DSL Method
 * @param name Document Name
 * @param root Document Root Entity
 * @param build Constructor (optional)
 */
fun document(name: String, root: Entity, build: Document.() -> Unit = {}) =
    Document(name, root).apply {
        build(this)
    }

/**
 * DSL Method
 * @param name Entity name
 * @param text Entity text (optional)
 * @param attributes Attributes List (optional)
 * @param children Children List (optional)
 * @param build Constructor (optional)
 */
fun Document.entity(name: String, text: String = "", attributes: MutableList<Attribute> = mutableListOf(), children: MutableList<Entity> = mutableListOf(), build: Entity.() -> Unit = {}) =
    Entity(name, text, getRoot(), attributes, children).apply {
        build(this)
    }

/**
 * DSL Method
 * @param name Entity name
 * @param text Entity text (optional)
 * @param attributes Attributes List (optional)
 * @param children Children List (optional)
 * @param build Constructor
 */
fun Entity.entity(name: String, text: String = "", attributes: MutableList<Attribute> = mutableListOf(), children: MutableList<Entity> = mutableListOf(), build: Entity.() -> Unit = {}) =
    Entity(name,text, this, attributes, children).apply {
        build(this)
    }

/**
 * DSl Method
 * @param name Attribute Name
 * @param value Attribute Value
 * @param build Constructor
 */
fun Entity.attribute(name: String, value: String, build: Attribute.() -> Unit = {}) =
    this.addAttribute(Attribute(name, value).apply { build(this) })

/**
 * DSL Method
 * @param txt Text to Add
 */
fun Entity.text(txt: String) =
    this.setText(txt)
