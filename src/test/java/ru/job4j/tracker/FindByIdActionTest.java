package ru.job4j.tracker;


import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FindByIdActionTest {
    @Test
    public void whenExecute() {
        var itemName = "test item";
        Item item = new Item(itemName);
        item.setId(1);

        Output out = new StubOutput();
        Tracker tracker = new Tracker();
        tracker.add(new Item(itemName));
        ShowByIdAction showByIdAction = new ShowByIdAction(out);

        Input input = mock(Input.class);
        when(input.askInt(any(String.class))).thenReturn(1);
        showByIdAction.execute(input, tracker);
        assertThat(out.toString().contains("name: test item"), is(true));
    }

    @Test
    public void whenExecuteWithInvalidId() {
        var itemName = "test item";
        Item item = new Item(itemName);
        item.setId(1);

        Output out = new StubOutput();
        Tracker tracker = new Tracker();
        tracker.add(new Item(itemName));
        ShowByIdAction showByIdAction = new ShowByIdAction(out);

        Input input = mock(Input.class);
        when(input.askInt(any(String.class))).thenReturn(5);

        showByIdAction.execute(input, tracker);

        assertThat(out.toString().contains("Заявка с введенным id: 5 не найдена."), is(true));
    }
}
