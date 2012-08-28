package com.googlecode.funclate;

import com.googlecode.funclate.json.Json;
import com.googlecode.totallylazy.Pair;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MutableModelTest extends ModelContract {
    @Override
    protected Model createModel() {
        return Model.mutable.model();
    }

    @Override
    protected Model createModel(Iterable<Pair<String, Object>> values) {
        return Model.mutable.model(values);
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
        Map<String, Object> users = (Map<String, Object>) root.get("users");
        List<Map<String, Object>> user = (List<Map<String, Object>>) users.get("user");

        Map<String, Object> dan = user.get(0);
        MatcherAssert.assertThat((String) dan.get("name"), is("Dan"));
        MatcherAssert.assertThat((String) dan.get("tel"), is("34567890"));

        Map<String, Object> mat = user.get(1);
        MatcherAssert.assertThat((String) mat.get("name"), is("Mat"));
        MatcherAssert.assertThat((String) mat.get("tel"), is("978532"));

        // reverse it

        Model reversed = MutableModel.fromMap(root);
        MatcherAssert.assertThat(reversed, is(original));
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
        Model result = MutableModel.parse(serialized);
        MatcherAssert.assertThat(result, is(original));
    }


    @Test
    public void shouldPreserveOrderingWhenConvertingToJson() throws Exception {
        Model original = createModel().
                add("2", "2").add("2", "3").add("1", "1");

        MatcherAssert.assertThat(original, equalTo(MutableModel.parse(Json.toJson(original))));
    }
}
