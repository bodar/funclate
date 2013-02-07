package com.googlecode.funclate;

import com.googlecode.totallylazy.Arrays;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

import static com.googlecode.funclate.Model.functions.mergeFlattenChildren;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Sequences.one;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.list;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

abstract public class ModelContract {
    protected Model createModel() {
        return modelFactory().create();
    }

    protected Model createModel(Iterable<Pair<String, Object>> values) {
        return modelFactory().create(values);
    }

    protected Model fromMap(Map<String, Object> root) {
        return modelFactory().create(root);
    }

    protected Model parse(String serialized) {
        return modelFactory().create(serialized);
    }

    protected Model fromProperties(Properties properties) {
        return modelFactory().create(properties);
    }

    protected abstract ModelFactory modelFactory();

    @Test
    public void supportsSingleValues() throws Exception {
        Model model = createModel().
                add("key", "value");
        assertThat(model.get("key", String.class), is("value"));
    }

    @Test
    public void supportsGetOption() throws Exception {
        Model model = createModel().
                add("key", "value");
        assertThat(model.getOption("key", String.class), is(Option.some("value")));
    }

    @Test
    public void supportsSet() throws Exception {
        Model model = createModel().add("key", "value").set("key", "foo");
        assertThat(model.get("key", String.class), is("foo"));
    }

    @Test
    public void returnsEmptyListWhenNoValues() throws Exception {
        assertThat(createModel().getValues("key"), is(empty()));
    }

    @Test
    public void supportsMultiValues() throws Exception {
        Model model = createModel().
                add("key", "one").
                add("key", "two");
        assertThat(model.getValues("key", String.class), hasExactly("one", "two"));
    }

    @Test
    public void supportsListMultiValues() throws Exception {
        Model model = createModel().
                add("key", Arrays.list("one")).
                add("key", Arrays.list("two"));
        assertThat(model.getValues("key", String.class), hasExactly("one", "two"));
    }

    @Test
    public void supportsSequenceMultiValues() throws Exception {
        Model model = createModel().
                add("key", sequence("one")).
                add("key", sequence("two"));
        assertThat(model.getValues("key", String.class), hasExactly("one", "two"));
    }

    @Test
    public void supportsImmuableListMultiValues() throws Exception {
        Model model = createModel().
                add("key", list("one")).
                add("key", list("two"));
        assertThat(model.getValues("key", String.class), hasExactly("one", "two"));
    }

    @Test
    public void alwaysReturnsACopyOfValues() throws Exception {
        List<String> value = java.util.Arrays.asList("one", "two");
        Model model = createModel().
                add("key", value);
        assertThat(model.getValues("key", String.class), not(sameInstance(value)));
    }

    @Test
    public void canModifyEmptyListInModel() throws Exception {
        Model model = createModel();
        model.getValues("key", String.class).add("foo");
    }

    @Test
    public void multiValuesCanBeRetrievedAsTheFirstValue() throws Exception {
        Model model = createModel().
                add("key", "one").
                add("key", "two");
        assertThat(model.get("key", String.class), is("one"));
    }

    @Test
    public void shouldPreserveOrdering() throws Exception {
        Model original = createModel().
                add("1", "1").add("2", "2").add("2", "3");

        Set<Map.Entry<String, Object>> entries = original.entries();
        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
        Map.Entry<String, Object> first = iterator.next();
        Map.Entry<String, Object> second = iterator.next();
        assertThat(first.getKey(), is("1"));
        assertThat(first.getValue(), equalTo((Object) "1"));
        assertThat(second.getKey(), is("2"));
        assertThat(second.getValue(), equalTo((Object) new ArrayList<String>(Arrays.list("2", "3"))));
    }

    @Test
    public void supportsRemove() throws Exception {
        final Model model = createModel(one(Pair.<String, Object>pair("key", "value")));
        model.remove("key");
        model.add("key", "value");
        final Pair<Model, Option<String>> value = Pair.pair(createModel(), some("value"));
        final Pair<Model, Option<String>> key = model.remove("key", String.class);
        assertThat(key, is(value));
    }

    @Test
    public void shouldPreserveListOrderingWhenConvertingToJson() throws Exception {
        Model original = createModel().
                add("2", "3").add("2", "2");

        MatcherAssert.assertThat(original.toString(), equalTo("{\"2\":[\"3\",\"2\"]}"));
    }

    @Test
    public void canConvertToAMap() throws Exception {
        Model original = createModel().
                add("users", createModel().
                        add("user", createModel().
                                add("name", "Dan").
                                add("tel", "34567890")).
                        add("user", createModel().
                                add("name", "Mat").
                                add("tel", "978532")));

        Map<String, Object> root = original.toMap();
        Map<String, Object> users = cast(root.get("users"));
        List<Map<String, Object>> user = cast(users.get("user"));

        Map<String, Object> dan = user.get(0);
        MatcherAssert.assertThat((String) dan.get("name"), is("Dan"));
        MatcherAssert.assertThat((String) dan.get("tel"), is("34567890"));

        Map<String, Object> mat = user.get(1);
        MatcherAssert.assertThat((String) mat.get("name"), is("Mat"));
        MatcherAssert.assertThat((String) mat.get("tel"), is("978532"));

        // reverse it

        Model reversed = fromMap(root);
        assertEquals(reversed, original);
    }

    @Test
    public void supportsSimpleMerge() throws Exception {
        Model big = createModel().
                add("users", createModel().
                        add("user", createModel().
                                add("Dan", createModel().
                                        add("hair", "short")).
                                add("Ray", createModel().
                                        add("hair", "none").
                                        add("accent", "evil"))));

        Model small = createModel().
                add("users", createModel().
                        add("user", createModel().
                                add("Ray", createModel().
                                        add("eyes", "brown"))));

        Model expected = Model.persistent.model().
                add("users", Model.persistent.model().
                        add("user", Model.persistent.model().
                                add("Dan", Model.persistent.model().
                                        add("hair", "short")).
                                add("Ray", Model.persistent.model().
                                        add("hair", "none").
                                        add("accent", "evil").
                                        add("eyes", "brown"))));

        assertThat(Model.methods.mergeFlattenChildren(big, small), is(expected));
        assertThat(Model.methods.mergeFlattenChildren(small, big), is(expected));
    }

    @Test
    public void supportsMergeWith2SameElementsInSameBranch() throws Exception {
        Model original = createModel().
                add("users", createModel().
                        add("user", createModel().
                                add("name", "Dan").
                                add("tel", "34567890")).
                        add("user", createModel().
                                add("name", "Mat").
                                add("tel", "978532")));
        Model second = createModel().
                add("users", createModel().
                        add("user", createModel().
                                add("name", "Alex").
                                add("dress-sense", "snappy"))
                );
        Model expected = Model.persistent.model().
                add("users", Model.persistent.model()
                        .add("user", Model.persistent.model().
                                add("name", "Dan").
                                add("tel", "34567890"))
                        .add("user", Model.persistent.model().
                                add("name", "Mat").
                                add("tel", "978532"))
                        .add("user", Model.persistent.model().
                                add("name", "Alex").
                                add("dress-sense", "snappy"))
                );
        Model actual = Model.methods.mergeFlattenChildren(original, second);
        assertThat(actual, is(expected));

        expected = Model.persistent.model().
                add("users", Model.persistent.model()
                        .add("user", Model.persistent.model().
                                add("name", "Alex").
                                add("dress-sense", "snappy"))
                        .add("user", Model.persistent.model().
                                add("name", "Dan").
                                add("tel", "34567890"))
                        .add("user", Model.persistent.model().
                                add("name", "Mat").
                                add("tel", "978532"))
                        );

        actual = Model.methods.mergeFlattenChildren(second, original);
        assertThat(actual, is(expected));
    }

    @Test
    public void supportsMergeWithEmptyList() throws Exception {
        Model original = createModel().
                add("users", createModel().
                        add("dev", createModel().
                                add("name", "Dan").
                                add("tel", "34567890")));
        Model second = createModel().
                add("users", createModel().
                        add("user", createModel().
                                add("name", "Alex").
                                add("dress-sense", "snappy"))
                );
        Model expected = Model.persistent.model().
                add("users", Model.persistent.model()
                        .add("user", Model.persistent.model().
                                add("name", "Alex").
                                add("dress-sense", "snappy"))
                        .add("dev", Model.persistent.model().
                                add("name", "Dan").
                                add("tel", "34567890"))
                );
        Model actual = Model.methods.mergeFlattenChildren(original, second);
        assertThat(actual, is(expected));

        expected = Model.persistent.model().
                add("users", Model.persistent.model()
                        .add("dev", Model.persistent.model().
                                add("name", "Dan").
                                add("tel", "34567890"))
                        .add("user", Model.persistent.model().
                                add("name", "Alex").
                                add("dress-sense", "snappy"))
                );

        actual = Model.methods.mergeFlattenChildren(second, original);
        assertThat(actual, is(expected));
    }

    @Test
    public void supportsMergeWithListOfOneElement() throws Exception {
        Model original = createModel().
                add("users", createModel().
                        add("user", createModel().
                                add("name", "Dan").
                                add("tel", "34567890")).
                        add("user", createModel().
                                add("name", "Mat").
                                add("tel", "978532")));

        Model second = createModel().
                add("users", createModel().
                        add("user", createModel().
                                add("name", "Alex").
                                add("dress-sense", "snappy"))
                        );

        Model expected = Model.persistent.model().
                add("users", Model.persistent.model()
                        .add("user", Model.persistent.model().
                                add("name", "Dan").
                                add("tel", "34567890"))
                        .add("user", Model.persistent.model().
                                add("name", "Mat").
                                add("tel", "978532"))
                        .add("user", Model.persistent.model().
                                add("name", "Alex").
                                add("dress-sense", "snappy"))

                );

        Model actual = Model.methods.mergeFlattenChildren(original, second);
        assertThat(actual, is(expected));


        expected = Model.persistent.model().
                add("users", Model.persistent.model().
                        add("user", Model.persistent.model().
                                add("name", "Alex").
                                add("dress-sense", "snappy")).
                        add("user", Model.persistent.model().
                                add("name", "Dan").
                                add("tel", "34567890")).
                        add("user", Model.persistent.model().
                                add("name", "Mat").
                                add("tel", "978532"))
                );

        actual = Model.methods.mergeFlattenChildren(second, original);
        assertThat(actual, is(expected));
    }

    @Test
    public void supportsMergeWithList() throws Exception {
        Model original = createModel().
                add("users", createModel().
                        add("user", createModel().
                                add("name", "Dan").
                                add("tel", "34567890")).
                        add("user", createModel().
                                add("name", "Mat").
                                add("tel", "978532")));

        Model second = createModel().
                add("users", createModel().
                        add("user", createModel().
                                add("name", "Alex").
                                add("dress-sense", "snappy")).
                        add("user", createModel().
                                add("name", "Stu").
                                add("tel", "999")));

        Model expected = Model.persistent.model().
                add("users", Model.persistent.model().
                        add("user", Model.persistent.model().
                                add("name", "Dan").
                                add("tel", "34567890")).
                        add("user", Model.persistent.model().
                                add("name", "Mat").
                                add("tel", "978532")).
                        add("user", Model.persistent.model().
                                add("name", "Alex").
                                add("dress-sense", "snappy")).
                        add("user", Model.persistent.model().
                                add("name", "Stu").
                                add("tel", "999"))

                );

        assertThat(Model.methods.mergeFlattenChildren(original, second), is(expected));

        expected = Model.persistent.model().
                add("users", Model.persistent.model()
                        .add("user", Model.persistent.model().
                                add("name", "Alex").
                                add("dress-sense", "snappy"))
                        .add("user", Model.persistent.model().
                                add("name", "Stu").
                                add("tel", "999"))
                        .add("user", Model.persistent.model().
                                add("name", "Dan").
                                add("tel", "34567890"))
                        .add("user", Model.persistent.model().
                                add("name", "Mat").
                                add("tel", "978532"))
                );

        assertThat(Model.methods.mergeFlattenChildren(second, original), is(expected));
    }


    @Test
    @Ignore
    public void flattenMergeIsCommutative() throws Exception {
        Model a = createModel().add("sharedKey", createModel().add("aModel", "aSharedValue"));
        Model c = createModel().add("sharedKey", createModel().add("cModel", "cSharedValue"));
        Model b = createModel().add("sharedKey", createModel().add("bModel", "bSharedValue")).add("sharedKey", createModel().add("dModel", "dSharedValue"));

        assertThat(sequence(a, c, b).reduce(mergeFlattenChildren), CoreMatchers.is(sequence(a,b,c).reduce(mergeFlattenChildren)));
    }

    @Test
    public void supportsConvertingToStringAndBack() throws Exception {
        Model original = createModel().
                add("users", createModel().
                        add("user", createModel().
                                add("name", "Dan").
                                add("tel", "34567890")).
                        add("user", createModel().
                                add("name", "Mat").
                                add("tel", "978532")));

        String serialized = original.toString();
        Model result = parse(serialized);
        MatcherAssert.assertThat(result, is(original));
    }

    @Test
    public void canConvertFromProperties() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("users.stuart.awesomeness", "extreme");
        properties.setProperty("users.dan.awesomeness", "mega");
        properties.setProperty("users.stuart.shoesize", "10.5");
        properties.setProperty("users.raymond.awesomeness", "totes amazeballs");

        Model model = fromProperties(properties);
        assertThat(model.get("users", Model.class).get("stuart", Model.class).get("awesomeness", String.class), is("extreme"));
        assertThat(model.get("users", Model.class).get("stuart", Model.class).get("shoesize", String.class), is("10.5"));
        assertThat(model.get("users", Model.class).get("dan", Model.class).get("awesomeness", String.class), is("mega"));
        assertThat(model.get("users", Model.class).get("raymond", Model.class).get("awesomeness", String.class), is("totes amazeballs"));
    }
}
