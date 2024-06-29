package sn.ept.git.seminaire.poc.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.data.Index;
import org.assertj.core.util.Files;
import org.junit.jupiter.api.Test;
import sn.ept.git.seminaire.poc.demo.calculator.Calculator;
import sn.ept.git.seminaire.poc.demo.calculator.ICalculator;
import sn.ept.git.seminaire.poc.demo.exception.DivisionByZeroException;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class DAssertJMatchersTest {


    @Test
    void test_string_matchers() {
        String devise = "Sagesse devoir";

        assertThat(devise)
                .as("If test fails display this message.")
                .isEqualTo("Sagesse devoir")
                .isEqualToIgnoringCase("Sagesse DEVOIR")
                .startsWith("S")
                .doesNotStartWith("age")
                .endsWith("r")
                .doesNotEndWith("re")
                .containsIgnoringCase("Devoir")
                .hasSize(14);
    }

    @Test
    void test_list_matchers() {
        List<String> list = Arrays.asList("Java", "Python", "Scala");
        assertThat(list)
                .as("message if an error occures")
                .hasSize(3)
                .contains("Java", "Scala")
                .contains("Java", Index.atIndex(0))
                .contains("Python", Index.atIndex(1))
                .contains("Scala", Index.atIndex(2))
                .doesNotContain("PHP")
                .allMatch(item->item.length()>=4)
                .anyMatch(item->item.length()<5)
                .noneMatch(item->item.length()>10);

    }

    @Test
    void test_map_matchers() {

        Map<String, String> map = new HashMap<>();
        map.put("nom", "DIC 1");
        map.put("departement", "GIT");

        //map.put("ecole", "EPT");
        ///map.put("pays", "Senegal");

        assertThat(map)
                .hasSize(2)
                .containsOnlyKeys("nom","departement")
                .containsKey("nom")
                .doesNotContainKey("une_cle")
                .doesNotContainKeys("key1","key2")
                .doesNotContainEntry("nom","DIC 3")
                .doesNotContainValue("PFE")
                .extractingByKey("nom", as(InstanceOfAssertFactories.STRING))
                .isEqualToIgnoringCase("DIC 1")
                .startsWith("D")
                .doesNotStartWith("ept");
    }


    @Test
    void test_exception_matchers() {
        ICalculator calculator = new Calculator();
        assertThatThrownBy(
                    () -> calculator.divide(1, 0)
                )
                .isInstanceOf(DivisionByZeroException.class)
                .hasMessageContaining("zero")
                .hasMessage(Calculator.DIVIDE_BY_ZERO);
    }

    @Test
    void test_optional_matchers() {
        Optional<Integer> optional =Optional.ofNullable(Integer.MAX_VALUE);
        assertThat(optional)
                .isNotNull()
                .isNotEmpty() //same as isPresent
                .isPresent() //same as isNotEmpty
                .get()
                .isExactlyInstanceOf(Integer.class)
                .isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    void test_compare_fields() {

         class Dog {
            public String name;
            public double weight;
            public Color color;
            public double random;

             public Dog(String name, double weight,Color color) {
                 this.name = name;
                 this.weight = weight;
                 this.color = color;
                 this.random= RandomUtils.nextDouble();
             }
         }

        Dog whiteDog = new Dog("Riki", 5.25, Color.WHITE);
        Dog grayDog = new Dog("Riki", 5.25, Color.GRAY);

        assertThat(whiteDog)
                .isNotEqualTo(grayDog)
                .isNotSameAs(grayDog);

        assertThat(whiteDog)
                .usingRecursiveComparison()
                .ignoringFields("color","random")
                .isEqualTo(grayDog);
    }


    @Test
    void test_directory() {
        File dir= Files.newTemporaryFolder();
        //dir.setReadable(true);
        //dir.setWritable(true);
        assertThat(dir)
                .exists()
                .isDirectory()
                .canRead()
                .canWrite();

    }

    @Test
    void test_file() {
        File file= Files.newTemporaryFile();
        file.setReadable(true);
        file.setWritable(false);
        assertThat(file)
                .exists()
                .isFile()
                .canRead();

    }

}


