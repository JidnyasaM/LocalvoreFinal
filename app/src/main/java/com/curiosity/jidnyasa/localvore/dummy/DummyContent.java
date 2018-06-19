package com.curiosity.jidnyasa.localvore.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyDashboard> ITEMS = new ArrayList<DummyDashboard>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyDashboard> ITEM_MAP = new HashMap<String, DummyDashboard>();

    private static final int COUNT = 6;

    static {
        DummyDashboard dd;
        dd = new DummyDashboard("Jungkook","Buddha Bowl","1. Preheat oven to 425 degrees F. Spread sweet potatoes and red onions onto a large baking sheet. Drizzle with about 1 tablespoon of olive oil. Season with salt and pepper and toss to coat. Bake for 20-25 minutes, until the sweet potatoes are tender. \n" +
                "Meanwhile, make chicken. Heat 1 tablespoon of olive oil in a large skillet. Season chicken all over with salt, pepper, garlic powder and ground ginger. Add chicken to skillet and cook for 6-8 minutes pre side, or until cooked through. Let rest for 10 minutes, then cut each breast into 1\" pieces.\n" +
                "Make dressing. Whisk together garlic, soy sauce, peanut butter, honey and lime juice until evenly combined. Whisk in sesame oil and 1 tablespoon of olive oil until smooth. \n" +
                "Divide rice between bowls. Top with sweet potatoes, chicken, avocado and baby spinach. Sprinkle with cilantro and sesame seeds and drizzle dressing on top. ");
        ITEMS.add(dd);
        dd = new DummyDashboard("RM","Healthy","Oil & Honey");
        ITEMS.add(dd);
        dd = new DummyDashboard("Jin","Which is better?","Sugar & Honey");
        ITEMS.add(dd);
        dd = new DummyDashboard("Suga","Ayurvedic","Tumeric & Red chilli");
        ITEMS.add(dd);
        dd = new DummyDashboard("Hoseok","Fruits", "Apple");
        ITEMS.add(dd);
        dd = new DummyDashboard("V","Fresh","Strawberry");
        ITEMS.add(dd);
        dd = new DummyDashboard("Jimin","Kimchi","Avacado");
        ITEMS.add(dd);
    }

    /**
     * A dummy item representing a piece of content.
     */

    public static class DummyDashboard {
        public final String recipeUserName;
        public final String recipeNameTitle;
        public final String recipeDesc;

        public DummyDashboard(String recipeUserName, String recipeNameTitle, String recipeDesc) {
            this.recipeUserName = recipeUserName;
            this.recipeNameTitle = recipeNameTitle;
            this.recipeDesc = recipeDesc;
        }

        @Override
        public String toString() {
            return recipeNameTitle+", "+recipeDesc;
        }
    }
}
