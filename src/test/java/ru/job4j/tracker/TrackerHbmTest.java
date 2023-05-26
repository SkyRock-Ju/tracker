package ru.job4j.tracker;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class TrackerHbmTest {

    @Test
    public void whenAddNewItemThenTrackerHasSameItem() {
        try (var tracker = new HbmTracker()) {
            Item item = new Item();
            item.setName("test1");
            tracker.add(item);
            Item result = tracker.findById(item.getId());
            assertThat(result.getName(), is(item.getName()));
        }
    }

    @Test
    public void whenFindByName() {
        try (var tracker = new HbmTracker()) {
            tracker.add(new Item("bug"));
            tracker.add(new Item("name"));
            Item item = tracker.add(new Item("first"));
            assertThat(tracker.findByName("first").get(0).getName(), is(item.getName()));
        }
    }

    @Test
    public void whenReplace() {
        try (var tracker = new HbmTracker()) {
            Item bug = new Item("Bug");
            tracker.add(bug);
            int id = bug.getId();
            Item bugWithDesc = new Item("Bug with description");
            tracker.replace(id, bugWithDesc);
            assertThat(tracker.findById(id).getName(), is(bugWithDesc.getName()));
        }
    }

    @Test
    public void whenDelete() {
        try (var tracker = new HbmTracker()) {
            Item bug = new Item("Bug");
            tracker.add(bug);
            int id = bug.getId();
            tracker.delete(id);
            assertThat(tracker.findById(id).getName(), is(nullValue()));
        }
    }
}
