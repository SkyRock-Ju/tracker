package ru.job4j.tracker;

import org.junit.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DeleteActionTest {
    @Test
    public void whenExecute() {
        Output out = new StubOutput();
        Tracker tracker = new Tracker();
        tracker.add(new Item("test item"));
        DeleteAction deleteAction = new DeleteAction(out);

        Input input = mock(Input.class);
        when(input.askInt(any(String.class))).thenReturn(1);

        deleteAction.execute(input, tracker);

        assertThat(out.toString().contains("Заявка удалена успешно."), is(true));
        assertThat(tracker.findAll().size(), is(0));
    }

    @Test
    public void whenExecuteWithInvalidId() {
        Output out = new StubOutput();
        Tracker tracker = new Tracker();
        Item newItem = new Item("test item");
        tracker.add(newItem);
        DeleteAction deleteAction = new DeleteAction(out);

        Input input = mock(Input.class);
        when(input.askStr(any(String.class))).thenReturn("1");

        deleteAction.execute(input, tracker);

        assertThat(out.toString().contains("Ошибка удаления заявки."), is(true));
        assertThat(tracker.findAll(), is(List.of(newItem)));
    }
}
