package test.pivotal.pal.tracker;

import io.pivotal.pal.tracker.InMemoryTimeEntryRepository;
import io.pivotal.pal.tracker.TimeEntry;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryTimeEntryRepositoryTest {
    private InMemoryTimeEntryRepository repo;

    @Before
    public void setUp() throws Exception {
        repo = new InMemoryTimeEntryRepository();
    }

    @Test
    public void create() throws Exception {
        TimeEntry createdTimeEntry = repo.create(new TimeEntry(123L, 456L, LocalDate.parse("2017-01-08"), 8));


        TimeEntry expectedTimeEntry = new TimeEntry(1L, 123L, 456L, LocalDate.parse("2017-01-08"), 8);
        assertThat(createdTimeEntry).isEqualTo(expectedTimeEntry);

        TimeEntry readEntry = repo.find(createdTimeEntry.getId());
        assertThat(readEntry).isEqualTo(expectedTimeEntry);
    }

    @Test
    public void find() throws Exception {
        repo.create(new TimeEntry(123L, 456L, LocalDate.parse("2017-01-08"), 8));

        TimeEntry expected = new TimeEntry(1L, 123L, 456L, LocalDate.parse("2017-01-08"), 8);
        TimeEntry readEntry = repo.find(1L);
        assertThat(readEntry).isEqualTo(expected);
    }

    @Test
    public void list() throws Exception {
        repo.create(new TimeEntry(123L, 456L, LocalDate.parse("2017-01-08"), 8));
        repo.create(new TimeEntry(789L, 654L, LocalDate.parse("2017-01-07"), 4));


        List<TimeEntry> actualList = repo.list();


        List<TimeEntry> expected = asList(
                new TimeEntry(1L, 123L, 456L, LocalDate.parse("2017-01-08"), 8),
                new TimeEntry(2L, 789L, 654L, LocalDate.parse("2017-01-07"), 4)
        );
        assertThat(actualList).isEqualTo(expected);
    }

    @Test
    public void update() throws Exception {
        TimeEntry created = repo.create(new TimeEntry(123L, 456L, LocalDate.parse("2017-01-08"), 8));

        TimeEntry updatedEntry = repo.update(
                created.getId(),
                new TimeEntry(321L, 654L, LocalDate.parse("2017-01-09"), 5));

        TimeEntry expected = new TimeEntry(created.getId(), 321L, 654L, LocalDate.parse("2017-01-09"), 5);
        assertThat(updatedEntry).isEqualTo(expected);
        assertThat(repo.find(created.getId())).isEqualTo(expected);
    }

    @Test
    public void delete() throws Exception {
        TimeEntry created = repo.create(new TimeEntry(123L, 456L, LocalDate.parse("2017-01-08"), 8));

        repo.delete(created.getId());
        assertThat(repo.list()).isEmpty();
    }

    @Test
    public void deleteOneItemOnly() throws Exception {
        TimeEntry created = repo.create(new TimeEntry(123L, 456L, LocalDate.parse("2017-01-08"), 8));
        repo.create(new TimeEntry(456L, 789L, LocalDate.parse("2017-01-09"), 5));

        repo.delete(created.getId());
        assertThat(repo.list().size()).isEqualTo(1);
    }
}
