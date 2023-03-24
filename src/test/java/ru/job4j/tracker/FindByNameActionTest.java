package ru.job4j.tracker;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FindByNameActionTest {

    @Test
    public void whenExecute() {
        var name = "test item";
        Item item1 = new Item(name);
        item1.setId(1);
        Item item2 = new Item(name);
        item2.setId(2);

        Output out = new StubOutput();
        Tracker tracker = new Tracker();
        tracker.add(item1);
        tracker.add(item2);
        ShowByNameAction showByNameAction = new ShowByNameAction(out);

        Input input = mock(Input.class);
        when(input.askStr(any(String.class))).thenReturn(name);

        showByNameAction.execute(input, tracker);

        var expOut = item1 + System.lineSeparator() + item2;
        assertThat(out.toString().contains(expOut), is(true));
    }

    @Test
    public void whenExecuteWithNotExistingName() {
        var name = "test item";

        Output out = new StubOutput();
        Tracker tracker = new Tracker();
        tracker.add(new Item(name));
        ShowByNameAction showByNameAction = new ShowByNameAction(out);

        Input input = mock(Input.class);
        when(input.askStr(any(String.class))).thenReturn("1");

        showByNameAction.execute(input, tracker);

        assertThat(out.toString().contains("Заявки с именем: 1 не найдены"), is(true));
    }
}
