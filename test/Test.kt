import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.io.File

class Test {
    // Document Class Tests
    @Test
    fun testDocGetRoot(){
        val plano = Entity("plano")
        val doc = Document("doc", plano)
        assertEquals(plano, doc.getRoot())
    }

    @Test
    fun testDocGetName(){
        val doc = Document("name", Entity("a"))
        assertEquals("name", doc.getName())
    }

    @Test
    fun testDocSetName(){
        val doc = Document("name", Entity("a"))
        doc.setName("new_name")
        assertEquals("new_name", doc.getName())
    }

    @Test
    fun testDocSetRoot(){
        val doc = Document("name", Entity("a"))
        val ent = Entity("name")
        doc.setRoot(ent)
        assertEquals(ent, doc.getRoot())
    }

    @Test
    fun testDocAddAttribute(){
        val plano = Entity("plano")
        val doc = Document("doc", plano)
        doc.addAttribute("plano", "added", "value")
        assertEquals("added", plano.getAttributes()[0].getName() )
        assertEquals("value", plano.getAttributes()[0].getValue())
    }

    @Test
    fun testDocRenameEntity(){
        val plano = Entity("plano")
        val doc = Document("doc", plano)
        doc.renameEntity("plano", "plano_renamed")
        assertEquals("plano_renamed", doc.getRoot().getName())
    }

    @Test
    fun testDocRenameAttribute(){
        val plano = Entity("plano")
        val doc = Document("doc", plano)
        doc.addAttribute("plano", "added", "value")
        doc.renameAttribute("plano", "added", "added_renamed")
        assertEquals("added_renamed", doc.getRoot().getAttributes()[0].getName())
    }

    @Test
    fun testDocRemoveEntity(){
        val plano = Entity("plano")
        val ent1 = Entity("one", father = plano)
        val ent2 = Entity("two", father = plano)
        val doc = Document("doc", plano)
        doc.removeEntity("one")
        assertEquals(mutableListOf(ent2), doc.getRoot().getChildren())
    }

    @Test
    fun testDocPrettyPrint(){
        val plano = Entity("plano")
        val doc = Document("doc", plano)
        val curso = Entity("curso", text="Mestrado em Engenharia Informática", father = plano)
        val fuc = Entity("fuc", father = plano)
        val att = Attribute("codigo", "M4310")
        fuc.addAttribute(att)
        val nome = Entity("nome", text = "Programação Avançada", father=fuc)
        val ects = Entity("ects", text = "6.0", father=fuc)
        val avaliacao = Entity("avaliacao", father=fuc)
        val componente1 = Entity("componente", father=avaliacao)
        val att2 = Attribute("nome", "Quizzes")
        val att3 = Attribute("peso", "20%")
        componente1.addAttribute(att2)
        componente1.addAttribute(att3)
        val componente2 = Entity("componente", father=avaliacao)
        val att4 = Attribute("nome", "Projeto")
        val att5 = Attribute("peso", "80%")
        componente2.addAttribute(att4)
        componente2.addAttribute(att5)
        val fuc2 = Entity("fuc", father = plano)
        val att6 = Attribute("codigo", "03782")
        fuc2.addAttribute(att6)
        val nome2 = Entity("nome", text = "Dissertação", fuc2)
        val ects2 = Entity("ects", text="42.0", fuc2)
        val avaliacao2 =  Entity("avaliacao", father=fuc2)
        val componente3 = Entity("componente", father=avaliacao2)
        val att7 = Attribute("nome", "Dissertação")
        val att8 = Attribute("peso", "60%")
        componente3.addAttribute(att7)
        componente3.addAttribute(att8)
        val componente4 = Entity("componente", father=avaliacao2)
        val att9 = Attribute("nome", "Apresentação")
        val att10 = Attribute("peso", "20%")
        componente4.addAttribute(att9)
        componente4.addAttribute(att10)
        val componente5 = Entity("componente", father=avaliacao2)
        val att11 = Attribute("nome", "Discussão")
        val att12 = Attribute("peso", "20%")
        componente5.addAttribute(att11)
        componente5.addAttribute(att12)
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<plano>\n" +
                "\t<curso>Mestrado em Engenharia Informática</curso>\n" +
                "\t<fuc codigo=\"M4310\">\n" +
                "\t\t<nome>Programação Avançada</nome>\n" +
                "\t\t<ects>6.0</ects>\n" +
                "\t\t<avaliacao>\n" +
                "\t\t\t<componente nome=\"Quizzes\" peso=\"20%\"/>\n" +
                "\t\t\t<componente nome=\"Projeto\" peso=\"80%\"/>\n" +
                "\t\t</avaliacao>\n" +
                "\t</fuc>\n" +
                "\t<fuc codigo=\"03782\">\n" +
                "\t\t<nome>Dissertação</nome>\n" +
                "\t\t<ects>42.0</ects>\n" +
                "\t\t<avaliacao>\n" +
                "\t\t\t<componente nome=\"Dissertação\" peso=\"60%\"/>\n" +
                "\t\t\t<componente nome=\"Apresentação\" peso=\"20%\"/>\n" +
                "\t\t\t<componente nome=\"Discussão\" peso=\"20%\"/>\n" +
                "\t\t</avaliacao>\n" +
                "\t</fuc>\n" +
                "</plano>\n", doc.prettyPrint())

    }

    @Test
    fun testDocPrettyPrintToFile(){
        val plano = Entity("plano")
        val doc = Document("doc", plano)
        val curso = Entity("curso", text="Mestrado em Engenharia Informática", father = plano)
        val fuc = Entity("fuc", father = plano)
        val att = Attribute("codigo", "M4310")
        fuc.addAttribute(att)
        val nome = Entity("nome", text = "Programação Avançada", father=fuc)
        val ects = Entity("ects", text = "6.0", father=fuc)
        val avaliacao = Entity("avaliacao", father=fuc)
        val componente1 = Entity("componente", father=avaliacao)
        val att2 = Attribute("nome", "Quizzes")
        val att3 = Attribute("peso", "20%")
        componente1.addAttribute(att2)
        componente1.addAttribute(att3)
        val componente2 = Entity("componente", father=avaliacao)
        val att4 = Attribute("nome", "Projeto")
        val att5 = Attribute("peso", "80%")
        componente2.addAttribute(att4)
        componente2.addAttribute(att5)
        val fuc2 = Entity("fuc", father = plano)
        val att6 = Attribute("codigo", "03782")
        fuc2.addAttribute(att6)
        val nome2 = Entity("nome", text = "Dissertação", fuc2)
        val ects2 = Entity("ects", text="42.0", fuc2)
        val avaliacao2 =  Entity("avaliacao", father=fuc2)
        val componente3 = Entity("componente", father=avaliacao2)
        val att7 = Attribute("nome", "Dissertação")
        val att8 = Attribute("peso", "60%")
        componente3.addAttribute(att7)
        componente3.addAttribute(att8)
        val componente4 = Entity("componente", father=avaliacao2)
        val att9 = Attribute("nome", "Apresentação")
        val att10 = Attribute("peso", "20%")
        componente4.addAttribute(att9)
        componente4.addAttribute(att10)
        val componente5 = Entity("componente", father=avaliacao2)
        val att11 = Attribute("nome", "Discussão")
        val att12 = Attribute("peso", "20%")
        componente5.addAttribute(att11)
        componente5.addAttribute(att12)
        val response = doc.prettyPrintToFile("out.txt")
        assertTrue(response.isFile)
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<plano>\n" +
                "\t<curso>Mestrado em Engenharia Informática</curso>\n" +
                "\t<fuc codigo=\"M4310\">\n" +
                "\t\t<nome>Programação Avançada</nome>\n" +
                "\t\t<ects>6.0</ects>\n" +
                "\t\t<avaliacao>\n" +
                "\t\t\t<componente nome=\"Quizzes\" peso=\"20%\"/>\n" +
                "\t\t\t<componente nome=\"Projeto\" peso=\"80%\"/>\n" +
                "\t\t</avaliacao>\n" +
                "\t</fuc>\n" +
                "\t<fuc codigo=\"03782\">\n" +
                "\t\t<nome>Dissertação</nome>\n" +
                "\t\t<ects>42.0</ects>\n" +
                "\t\t<avaliacao>\n" +
                "\t\t\t<componente nome=\"Dissertação\" peso=\"60%\"/>\n" +
                "\t\t\t<componente nome=\"Apresentação\" peso=\"20%\"/>\n" +
                "\t\t\t<componente nome=\"Discussão\" peso=\"20%\"/>\n" +
                "\t\t</avaliacao>\n" +
                "\t</fuc>\n" +
                "</plano>\n",  response.readText())
    }



    // Entity Tests
    @Test
    fun testEntGetName(){
        val plano = Entity("plano")
        assertEquals("plano", plano.getName())
    }

    @Test
    fun testEntGetText(){
        val plano = Entity("plano", text = "Teste123")
        assertEquals("Teste123", plano.getText())
    }

    @Test
    fun testEntGetFather(){
        val plano = Entity("plano")
        val curso = Entity("curso", father = plano)
        assertEquals(plano, curso.getFather())
    }

    @Test
    fun testEntGetChildren(){
        val plano = Entity("plano")
        val curso = Entity("curso", father = plano)
        val fuc = Entity("fuc", father = plano)
        assertEquals(mutableListOf(curso, fuc), plano.getChildren())
    }

    @Test
    fun testEntSetName(){
        val plano = Entity("plano")
        plano.setName("new_name")
        assertEquals("new_name", plano.getName())
    }

    @Test
    fun testEntSetText(){
        val plano = Entity("plano", text = "text")
        plano.setText("new_text")
        assertEquals("new_text", plano.getText())
    }

    @Test
    fun testEntSetFather(){
        val plano = Entity("plano")
        val ent = Entity("father")
        plano.setFather(ent)
        assertEquals(ent, plano.getFather())
    }

    @Test
    fun testEntSetAttributes(){
        val plano = Entity("plano")
        val att1 = Attribute("att1", "att1")
        val att2 = Attribute("att2", "att2")
        plano.setAttributes(mutableListOf(att1, att2))
        assertEquals(mutableListOf(att1, att2), plano.getAttributes())
    }

    @Test
    fun testEntSetChildren(){
        val plano = Entity("plano")
        val child1 = Entity("child1")
        val child2 = Entity("child2")
        plano.setChildren(mutableListOf(child2, child1))
        assertEquals(mutableListOf(child2, child1), plano.getChildren())
    }

    @Test
    fun testEntAddChild(){
        val plano = Entity("plano")
        val ent = Entity("child")
        plano.addChild(ent)
        assertEquals(mutableListOf(ent), plano.getChildren())
    }

    @Test
    fun testEntAddAttribute(){
        val plano = Entity("plano")
        val att = Attribute("att", "att")
        plano.addAttribute(att)
        assertEquals(mutableListOf(att), plano.getAttributes())
    }

    @Test
    fun testEntRemoveAttribute(){
        val plano = Entity("plano")
        val att = Attribute("att", "att")
        val att2 = Attribute("att2", "att2")
        plano.setAttributes(mutableListOf(att, att2))
        plano.removeAttribute(att)
        assertEquals(mutableListOf(att2), plano.getAttributes())
    }

    @Test
    fun testEntDeleteChild(){
        val plano = Entity("plano")
        val child1 = Entity("child1", father = plano)
        val child2 = Entity("child2", father = plano)
        plano.deleteChild(child2)
        assertEquals(mutableListOf(child1), plano.getChildren())
    }

    @Test
    fun testGetAttributes(){
        val att1 = Attribute("atributo", "123")
        val att2 = Attribute("atributo", "456")
        val plano = Entity("plano", attributes = mutableListOf(att1, att2))
        assertEquals(mutableListOf(att1, att2), plano.getAttributes())
    }

    //Attribute Tests
    @Test
    fun testAttGetName(){
        val att1 = Attribute("atributo", "123")
        assertEquals("atributo", att1.getName())
    }

    @Test
    fun testAttGetValue(){
        val att1 = Attribute("atributo", "123")
        assertEquals("123", att1.getValue())
    }

    @Test
    fun testAttSetName(){
        val att = Attribute("name", "123")
        att.setName("new_name")
        assertEquals("new_name", att.getName())
    }

    @Test
    fun testAttSetValue(){
        val att = Attribute("name", "123")
        att.setValue("new_value")
        assertEquals("new_value", att.getValue())
    }

    data class EvaluationComponent(
        val name: String,
        val weight: Int)

    @Test
    fun testTranslate(){
        val comp = EvaluationComponent("name", 100)
        val response = translate(comp)
        assertEquals(Entity("EvaluationComponent", children = mutableListOf(Entity("name"), Entity("weight"))), response)
    }
}