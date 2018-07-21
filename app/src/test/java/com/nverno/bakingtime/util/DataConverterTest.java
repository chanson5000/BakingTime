package com.nverno.bakingtime.util;

import com.nverno.bakingtime.model.Ingredient;

import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
//
//import static com.google.common.truth.Truth.assertThat;
//import static com.google.common.truth.Truth.assertWithMessage;
//
//import static org.assertj.core.api.Assertions.*;

public class DataConverterTest {

    @Test
    public void fromIngredientList() {

    }

    @Test
    public void toIngredientList() {
        String input = "["
                + "{\"quantity\":400,\"measure\":\"G\",\"ingredient\":\"sifted cake flour\"},"
                + "{\"quantity\":700,\"measure\":\"G\",\"ingredient\":\"granulated sugar\"}"
                + "]";

        List<Ingredient> output;

        Ingredient ingredientA = new Ingredient();
        ingredientA.setQuantity(400d);
        ingredientA.setMeasure("G");
        ingredientA.setName("sifted cake flour");

        Ingredient ingredientB = new Ingredient();
        ingredientB.setQuantity(700d);
        ingredientB.setMeasure("G");
        ingredientB.setName("granulated sugar");

        List<Ingredient> expected = new ArrayList<>(Arrays.asList(ingredientA, ingredientB));

        DataConverter dataConverter = new DataConverter();

        output = dataConverter.toIngredientList(input);

        Assert.assertEquals(output.get(1).getName(), expected.get(1).getName());
        Assert.assertEquals(output.get(1).getMeasure(), expected.get(1).getMeasure());
        Assert.assertEquals(output.get(1).getQuantity(), expected.get(1).getQuantity());

        Assert.assertEquals(output.get(0).getName(), expected.get(0).getName());
        Assert.assertEquals(output.get(0).getMeasure(), expected.get(0).getMeasure());
        Assert.assertEquals(output.get(0).getQuantity(), expected.get(0).getQuantity());
    }

    @Test
    public void fromStepList() {
    }

    @Test
    public void toStepList() {
    }
}