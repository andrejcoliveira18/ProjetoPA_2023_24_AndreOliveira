# About the Project

This is a Kotlin API for XML generation and manipulation through Kotlin objects. This library was developed in academic context for Advanced Program curricular course as part of the Master´s in Computer Engineering at ISCTE-IUL.
Project owner: André Oliveira nº98412.

# Functionalities

## Phase 1 - Model

1. Adding and Removing XML Entities
2. Adding, removing and edit Entity Attributes
3. Get mother and children Entities from an Entity
4. String format Pretty Print
5. Visitor
6. Globally add attributes to a Document (Giving entity name, attribute name and value)
7. Globally rename entities in Document (Giving old and new name)
8. Globally rename attributes in Document (Giving entity name, attribute old and new name)
9. Globally remove entities in Document (Giving entity name)
10. Globally remove attributes in Document (Giving entity name and attribute name)

### Micro-X-Path

In this phase was also implemented a functionality to get a list of entities that match with a given XPath.

## Phase 2 - Class Mapping - XML

In this phase, the main objective is to automatically obtain XML entities from Kotlin objects, based in the class structure.

## Phase 3 - Internal DSL (Optional)

In this phase, the main goal was to make the API easier to instantiate the XML models through internal Kotlin DSL.


# Examples

## Entity Creation

To create an entity with just a name

```kotlin
val ent = Entity("name")
```

To create an Entity with name and description text

```kotlin
val ent = Entity("name", "text")
```

To create an Entity with name and a given father

```kotlin
val ent = Entity("name", father=father_entity)
```

To create an Enrtity with name, text and specific attributes and children lists

```kotlin
val ent = Entity("name", "text", attributes=att_list, children=child_list)
```

## Document Creation

Only way to create a Document, giving a name and a root Entity

```kotlin
val doc = Document("name", root_entity)
```

## Attribute Creation

Only way to create an Attribute, giving the name and the attribute value

```kotlin
val att = Attribute("name", "value")
```

##  Adding Child to Entity

```kotlin
ent.addChild(ent2)
```

## Attribute Manipulation in Entity

Add Attribute

```kotlin
ent.addAttribute(att)
```

Remove Attribute

```kotlin
ent.removeAttribute(att)
```

## Get Entity father and children

Get father entity

```kotlin
ent.getFather()
```

Get children list

```kotlin
ent.getChildren()
```

## Document Pretty Print

Get a document pretty print in XML string

```kotlin
doc.prettyPrint()
```

## Using the Visitor

```kotlin
ent.accept {
  if (something) {
      it.do_something
  } 
}
true
```

## Globally Add Attribute to Document

```kotlin
doc.addAttribute(entity_name, attribute_name, attribute_value)
```

## Globally Rename Entity in Document

```kotlin
doc.renameEntity(old_name, new_name)
```

## Globally Rename Attribute in Document

```kotlin
doc.renameAttribute(entity_name, attribute_old_name, attribute_new_name)
```

## Globally Remove Entity in Document

```kotlin
doc.removeEntity(entity_name)
```

## Globally Remove Attribute in Document
```kotlin
doc.removeAttribute(entity_name, attribute_name)
```

## Micro X-Path

```kotlin
doc.microXPath(x_path)
```
Exemplo de XPath: entity1/entity2/entity3

# Translate Class to Entity

If you want to "translate" any data class to Entity, you can use translate(obj: Any) to do it.

Suppose that you have a data class named FUC and another one named "EvaluationComponent":

````kotlin
data class EvaluationComponent(
  val name: String,
  val weight: Int)

data class FUC(
  val code: String,
  val name: String,
  val ects: Double,
  val observations: String,
  val evaluation: List<EvaluationComponent>)
````

You can initialize them like:

````kotlin
val ca1 = EvaluationComponent("ca1", 10)
val ca2 = EvaluationComponent("ca2", 90)

val fuc = FUC("ABC123", "fuc", 6.0, "Advanced Programming", listOf(ca1, ca2))
````

With translate and pretty print:

````kotlin
val ent = translate(fuc)

println(ent.prettyPrint())
````

The result will be something like:

````xml
<?xml version="1.0" encoding="UTF-8"?>
 <FUC>
    <code>ABC123</code>
    <name>fuc</name>
    <ects>6.0</ects>
    <observations>Advanced Programming</observations>
    <evaluation>
        <EvaluationComponent>
            <name>ca1</name>
            <weight>10</weight>
		</EvaluationComponent>
        <EvaluationComponent>
            <name>ca2</name>
            <weight>90</weight>
        </EvaluationComponent>
    </evaluation>
 </FUC>

````

If you want to improve this translation, you can use some kotlin annotations.

Let´s take the previous FUC and EvaluationComponent classes. 

- For personalization related to Attribute vs Entity: It´s possible to add @XmlAttribute to a class property to make 
the property translation as attribute and not as a child entity (default).
- For free personalization of a String, you can add @XmlString before a property of type string to adapt the way it looks
in pretty print.
- For free personalization of class, you can add @XmlAdapter annotation to a class to adapt the final entity as you like.

With the following changes, we can observe some changes in the final result as it became more personalized

````kotlin
 interface AttributeAdapter<Any> {
     fun adapt(att: Any): String
 }

 class AddPercentage: AttributeAdapter<Int> {
     override fun adapt(att: Int): String {
         return "$att%"
     }
 }

 interface EntityAdapter {
     fun adapt(ent: Entity)
 }

 class FUCAdapter: EntityAdapter {
     override fun adapt(ent: Entity) {
         ent.addAttribute(Attribute("valor", "att"))
     }
 }

 data class EvaluationComponent(
     @XmlAttribute
     val name: String,
     @XmlAttribute
     @XmlString(AddPercentage::class)
     val weight: Int)

 @XmlAdapter(FUCAdapter::class)
 data class FUC(
     val code: String,
     val name: String,
     val ects: Double,
     val observations: String,
     val evaluation: List<EvaluationComponent>
 )
````

With this, the result should be something like:

````xml
<?xml version="1.0" encoding="UTF-8"?>
 <FUC valor="att">
	<code>ABC123</code>
	<name>fuc</name>
	<ects>6.0</ects>
	<observations>Advanced Programming</observations>
	<evaluation>
		<EvaluationComponent name="ca1" weight="10%"/>
		<EvaluationComponent name="ca2" weight="90%"/>
	</evaluation>
</FUC>
````

# Internal DSL

To make easier and more intuitive to create XML Entities, it's possible to create them using the implemented internal DSL.

````kotlin
 val b = document("doc", root_ent) {
   entity("FUC") {
       entity("name"){
           text("text")
           attribute("att", "val")
       }
       entity("Component"){
           text("text")
       }
   }
 }
````

And the pretty print looks something like:

````xml
<?xml version="1.0" encoding="UTF-8"?>
 <plano>
    <FUC>
        <name>text</name>
        <Component>text</Component>
    </FUC>
</plano>
````

# Global Example

Reproduce the following XML using this API classes.

```xml
<?xml version="1.0" encoding="UTF-8"?>
 <plan>
     <course>Computer Science Master's</course>
     <fuc code="M4310">
         <name>Advanced Programming</name>
         <ects>6.0</ects>
         <evaluation>
             <component name="Quizzes" weight="20%"/>
             <component name="Project" weight="80%"/>
         </evaluation>
     </fuc>
     <fuc code="03782">
         <name>Dissertation</name>
         <ects>42.0</ects>
         <evaluation>
             <component name="Dissertation" weight="60%"/>
             <component name="Presentation" weight="20%"/>
             <component name="Discussion" weight="20%"/>
         </evaluation>
     </fuc>
 </plan>
```
Using the DSL it´s pretty simple to reproduce that XML string.

```kotlin
val doc = document("doc", Entity("plan")){
    entity("course"){
        text("Computer Science Master's")
    }
    entity("fuc"){
        attribute("code", "M4310")
        entity("name"){
            text("Advanced Programming")
        }
        entity("ects"){
            text("6.0")
        }
        entity("evaluation"){
            entity("component"){
                attribute("name", "Quizzes")
                attribute("weight", "20%")
            }
            entity("component"){
                attribute("name", "Project")
                attribute("weight", "80%")
            }
        }
    }
    entity("fuc") {
        attribute("code", "03782")
        entity("name") {
                text("Dissertation")
        }
        entity("ects") {
            text("42.0")
        }
        entity("evaluation") {
            entity("component") {
                attribute("name", "Dissertation")
                attribute("weight", "60%")
        }
        entity("component") {
            attribute("name", "Presentation")
            attribute("weight", "20%")
        }
        entity("component") {
            attribute("name", "Discussion")
            attribute("weight", "20%")
        }
    }
}
}
```





