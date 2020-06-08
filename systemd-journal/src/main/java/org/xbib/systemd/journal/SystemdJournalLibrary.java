package org.xbib.systemd.journal;

import org.bridj.*;
import org.bridj.ann.Library;
import org.bridj.ann.Ptr;
import org.bridj.ann.Runtime;

@Library("systemd")
@Runtime(CRuntime.class)
public class SystemdJournalLibrary {

    static {
        BridJ.register();
    }

    public static final int SD_JOURNAL_LOCAL_ONLY = 1;

    public static final int SD_JOURNAL_RUNTIME_ONLY = 2;

    public static final int SD_JOURNAL_SYSTEM = 4;

    public static final int SD_JOURNAL_CURRENT_USER = 8;

    public static final int SD_JOURNAL_NOP = 0;

    public static final int SD_JOURNAL_APPEND = 1;

    public static final int SD_JOURNAL_INVALIDATE = 2;

    public static final String SD_ID128_FORMAT_STR = "%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x";

    @Ptr
    protected native static long sd_id128_to_string(sd_id128 id, @Ptr long s);

    public static int sd_id128_from_string(Pointer<Byte> s, Pointer<sd_id128> ret) {
        return sd_id128_from_string(Pointer.getPeer(s), Pointer.getPeer(ret));
    }

    protected native static int sd_id128_from_string(@Ptr long s, @Ptr long ret);

    public static int sd_id128_randomize(Pointer<sd_id128> ret) {
        return sd_id128_randomize(Pointer.getPeer(ret));
    }

    protected native static int sd_id128_randomize(@Ptr long ret);

    public static int sd_id128_get_machine(Pointer<sd_id128> ret) {
        return sd_id128_get_machine(Pointer.getPeer(ret));
    }

    protected native static int sd_id128_get_machine(@Ptr long ret);

    public static int sd_id128_get_boot(Pointer<sd_id128> ret) {
        return sd_id128_get_boot(Pointer.getPeer(ret));
    }

    protected native static int sd_id128_get_boot(@Ptr long ret);

    public static int sd_journal_print(int priority, Pointer<Byte> format, Object... varArgs1) {
        return sd_journal_print(priority, Pointer.getPeer(format), varArgs1);
    }

    protected native static int sd_journal_print(int priority, @Ptr long format, Object... varArgs1);

    public static int sd_journal_printv(int priority, Pointer<Byte> format, Object... ap) {
        return sd_journal_printv(priority, Pointer.getPeer(format), ap);
    }

    protected native static int sd_journal_printv(int priority, @Ptr long format, Object... ap);

    public static int sd_journal_send(Pointer<Byte> format, Object... varArgs1) {
        return sd_journal_send(Pointer.getPeer(format), varArgs1);
    }

    protected native static int sd_journal_send(@Ptr long format, Object... varArgs1);

    public static int sd_journal_sendv(Pointer<? extends StructObject> iov, int n) {
        return sd_journal_sendv(Pointer.getPeer(iov), n);
    }

    protected native static int sd_journal_sendv(@Ptr long iov, int n);

    public static int sd_journal_perror(Pointer<Byte> message) {
        return sd_journal_perror(Pointer.getPeer(message));
    }

    protected native static int sd_journal_perror(@Ptr long message);

    public static int sd_journal_print_with_location(int priority, Pointer<Byte> file, Pointer<Byte> line,
                                                     Pointer<Byte> func, Pointer<Byte> format, Object... varArgs1) {
        return sd_journal_print_with_location(priority, Pointer.getPeer(file), Pointer.getPeer(line),
                Pointer.getPeer(func), Pointer.getPeer(format), varArgs1);
    }

    protected native static int sd_journal_print_with_location(int priority, @Ptr long file, @Ptr long line,
                                                               @Ptr long func, @Ptr long format, Object... varArgs1);

    public static int sd_journal_printv_with_location(int priority, Pointer<Byte> file, Pointer<Byte> line,
                                                      Pointer<Byte> func, Pointer<Byte> format, Object... ap) {
        return sd_journal_printv_with_location(priority, Pointer.getPeer(file), Pointer.getPeer(line),
                Pointer.getPeer(func), Pointer.getPeer(format), ap);
    }

    protected native static int sd_journal_printv_with_location(int priority, @Ptr long file, @Ptr long line,
                                                                @Ptr long func, @Ptr long format, Object... ap);

    public static int sd_journal_send_with_location(Pointer<Byte> file, Pointer<Byte> line, Pointer<Byte> func,
                                                    Pointer<Byte> format, Object... varArgs1) {
        return sd_journal_send_with_location(Pointer.getPeer(file), Pointer.getPeer(line), Pointer.getPeer(func),
                Pointer.getPeer(format), varArgs1);
    }

    protected native static int sd_journal_send_with_location(@Ptr long file, @Ptr long line, @Ptr long func,
                                                              @Ptr long format, Object... varArgs1);

    public static int sd_journal_sendv_with_location(Pointer<Byte> file, Pointer<Byte> line, Pointer<Byte> func,
                                                     Pointer<? extends StructObject> iov, int n) {
        return sd_journal_sendv_with_location(Pointer.getPeer(file), Pointer.getPeer(line), Pointer.getPeer(func),
                Pointer.getPeer(iov), n);
    }

    protected native static int sd_journal_sendv_with_location(@Ptr long file, @Ptr long line, @Ptr long func,
                                                               @Ptr long iov, int n);

    public static int sd_journal_perror_with_location(Pointer<Byte> file, Pointer<Byte> line, Pointer<Byte> func,
                                                      Pointer<Byte> message) {
        return sd_journal_perror_with_location(Pointer.getPeer(file), Pointer.getPeer(line), Pointer.getPeer(func),
                Pointer.getPeer(message));
    }

    protected native static int sd_journal_perror_with_location(@Ptr long file, @Ptr long line, @Ptr long func,
                                                                @Ptr long message);

    public static int sd_journal_stream_fd(Pointer<Byte> identifier, int priority, int level_prefix) {
        return sd_journal_stream_fd(Pointer.getPeer(identifier), priority, level_prefix);
    }

    protected native static int sd_journal_stream_fd(@Ptr long identifier, int priority, int level_prefix);

    public static int sd_journal_open(Pointer<Pointer<sdJournal>> ret, int flags) {
        return sd_journal_open(Pointer.getPeer(ret), flags);
    }

    protected native static int sd_journal_open(@Ptr long ret, int flags);

    public static int sd_journal_open_directory(Pointer<Pointer<sdJournal>> ret,
                                                Pointer<Byte> path, int flags) {
        return sd_journal_open_directory(Pointer.getPeer(ret), Pointer.getPeer(path), flags);
    }

    protected native static int sd_journal_open_directory(@Ptr long ret, @Ptr long path, int flags);

    public static int sd_journal_open_files(Pointer<Pointer<sdJournal>> ret,
                                            Pointer<Pointer<Byte>> paths, int flags) {
        return sd_journal_open_files(Pointer.getPeer(ret), Pointer.getPeer(paths), flags);
    }

    protected native static int sd_journal_open_files(@Ptr long ret, @Ptr long paths, int flags);

    public static void sd_journal_close(Pointer<sdJournal> j) {
        sd_journal_close(Pointer.getPeer(j));
    }

    protected native static void sd_journal_close(@Ptr long j);

    public static int sd_journal_previous(Pointer<sdJournal> j) {
        return sd_journal_previous(Pointer.getPeer(j));
    }

    protected native static int sd_journal_previous(@Ptr long j);

    public static int sd_journal_next(Pointer<sdJournal> j) {
        return sd_journal_next(Pointer.getPeer(j));
    }

    protected native static int sd_journal_next(@Ptr long j);

    public static int sd_journal_previous_skip(Pointer<sdJournal> j, long skip) {
        return sd_journal_previous_skip(Pointer.getPeer(j), skip);
    }

    protected native static int sd_journal_previous_skip(@Ptr long j, long skip);

    public static int sd_journal_next_skip(Pointer<sdJournal> j, long skip) {
        return sd_journal_next_skip(Pointer.getPeer(j), skip);
    }

    protected native static int sd_journal_next_skip(@Ptr long j, long skip);

    public static int sd_journal_get_realtime_usec(Pointer<sdJournal> j, Pointer<Long> ret) {
        return sd_journal_get_realtime_usec(Pointer.getPeer(j), Pointer.getPeer(ret));
    }

    protected native static int sd_journal_get_realtime_usec(@Ptr long j, @Ptr long ret);

    public static int sd_journal_get_monotonic_usec(Pointer<sdJournal> j, Pointer<Long> ret,
                                                    Pointer<sd_id128> ret_boot_id) {
        return sd_journal_get_monotonic_usec(Pointer.getPeer(j), Pointer.getPeer(ret), Pointer.getPeer(ret_boot_id));
    }

    protected native static int sd_journal_get_monotonic_usec(@Ptr long j, @Ptr long ret, @Ptr long ret_boot_id);

    public static int sd_journal_set_data_threshold(Pointer<sdJournal> j, @Ptr long sz) {
        return sd_journal_set_data_threshold(Pointer.getPeer(j), sz);
    }

    protected native static int sd_journal_set_data_threshold(@Ptr long j, @Ptr long sz);

    public static int sd_journal_get_data_threshold(Pointer<sdJournal> j, Pointer<SizeT> sz) {
        return sd_journal_get_data_threshold(Pointer.getPeer(j), Pointer.getPeer(sz));
    }

    protected native static int sd_journal_get_data_threshold(@Ptr long j, @Ptr long sz);

    public static int sd_journal_get_data(Pointer<sdJournal> j, Pointer<Byte> field,
                                          Pointer<Pointer<?>> data, Pointer<SizeT> l) {
        return sd_journal_get_data(Pointer.getPeer(j), Pointer.getPeer(field), Pointer.getPeer(data), Pointer.getPeer(l));
    }

    protected native static int sd_journal_get_data(@Ptr long j, @Ptr long field, @Ptr long data, @Ptr long l);

    public static int sd_journal_enumerate_data(Pointer<sdJournal> j, Pointer<Pointer<?>> data,
                                                Pointer<SizeT> l) {
        return sd_journal_enumerate_data(Pointer.getPeer(j), Pointer.getPeer(data), Pointer.getPeer(l));
    }

    protected native static int sd_journal_enumerate_data(@Ptr long j, @Ptr long data, @Ptr long l);

    public static void sd_journal_restart_data(Pointer<sdJournal> j) {
        sd_journal_restart_data(Pointer.getPeer(j));
    }

    protected native static void sd_journal_restart_data(@Ptr long j);

    public static int sd_journal_add_match(Pointer<sdJournal> j, Pointer<?> data,
                                           @Ptr long size) {
        return sd_journal_add_match(Pointer.getPeer(j), Pointer.getPeer(data), size);
    }

    protected native static int sd_journal_add_match(@Ptr long j, @Ptr long data, @Ptr long size);

    public static int sd_journal_add_disjunction(Pointer<sdJournal> j) {
        return sd_journal_add_disjunction(Pointer.getPeer(j));
    }

    protected native static int sd_journal_add_disjunction(@Ptr long j);

    public static int sd_journal_add_conjunction(Pointer<sdJournal> j) {
        return sd_journal_add_conjunction(Pointer.getPeer(j));
    }

    protected native static int sd_journal_add_conjunction(@Ptr long j);

    public static void sd_journal_flush_matches(Pointer<sdJournal> j) {
        sd_journal_flush_matches(Pointer.getPeer(j));
    }

    protected native static void sd_journal_flush_matches(@Ptr long j);

    public static int sd_journal_seek_head(Pointer<sdJournal> j) {
        return sd_journal_seek_head(Pointer.getPeer(j));
    }

    protected native static int sd_journal_seek_head(@Ptr long j);

    public static int sd_journal_seek_tail(Pointer<sdJournal> j) {
        return sd_journal_seek_tail(Pointer.getPeer(j));
    }

    protected native static int sd_journal_seek_tail(@Ptr long j);

    public static int sd_journal_seek_monotonic_usec(Pointer<sdJournal> j, sd_id128 boot_id,
                                                     long usec) {
        return sd_journal_seek_monotonic_usec(Pointer.getPeer(j), boot_id, usec);
    }

    protected native static int sd_journal_seek_monotonic_usec(@Ptr long j, sd_id128 boot_id, long usec);

    public static int sd_journal_seek_realtime_usec(Pointer<sdJournal> j, long usec) {
        return sd_journal_seek_realtime_usec(Pointer.getPeer(j), usec);
    }

    protected native static int sd_journal_seek_realtime_usec(@Ptr long j, long usec);

    public static int sd_journal_seek_cursor(Pointer<sdJournal> j, Pointer<Byte> cursor) {
        return sd_journal_seek_cursor(Pointer.getPeer(j), Pointer.getPeer(cursor));
    }

    protected native static int sd_journal_seek_cursor(@Ptr long j, @Ptr long cursor);

    public static int sd_journal_get_cursor(Pointer<sdJournal> j, Pointer<Pointer<Byte>> cursor) {
        return sd_journal_get_cursor(Pointer.getPeer(j), Pointer.getPeer(cursor));
    }

    protected native static int sd_journal_get_cursor(@Ptr long j, @Ptr long cursor);

    public static int sd_journal_test_cursor(Pointer<sdJournal> j, Pointer<Byte> cursor) {
        return sd_journal_test_cursor(Pointer.getPeer(j), Pointer.getPeer(cursor));
    }

    protected native static int sd_journal_test_cursor(@Ptr long j, @Ptr long cursor);

    public static int sd_journal_get_cutoff_realtime_usec(Pointer<sdJournal> j, Pointer<Long> from,
                                                          Pointer<Long> to) {
        return sd_journal_get_cutoff_realtime_usec(Pointer.getPeer(j), Pointer.getPeer(from), Pointer.getPeer(to));
    }

    protected native static int sd_journal_get_cutoff_realtime_usec(@Ptr long j, @Ptr long from, @Ptr long to);

    public static int sd_journal_get_cutoff_monotonic_usec(Pointer<sdJournal> j, sd_id128 boot_id,
                                                           Pointer<Long> from, Pointer<Long> to) {
        return sd_journal_get_cutoff_monotonic_usec(Pointer.getPeer(j), boot_id, Pointer.getPeer(from), Pointer.getPeer(to));
    }

    protected native static int sd_journal_get_cutoff_monotonic_usec(@Ptr long j, sd_id128 boot_id, @Ptr long from,
                                                                     @Ptr long to);

    public static int sd_journal_get_usage(Pointer<sdJournal> j, Pointer<Long> bytes) {
        return sd_journal_get_usage(Pointer.getPeer(j), Pointer.getPeer(bytes));
    }

    protected native static int sd_journal_get_usage(@Ptr long j, @Ptr long bytes);

    public static int sd_journal_query_unique(Pointer<sdJournal> j, Pointer<Byte> field) {
        return sd_journal_query_unique(Pointer.getPeer(j), Pointer.getPeer(field));
    }

    protected native static int sd_journal_query_unique(@Ptr long j, @Ptr long field);

    public static int sd_journal_enumerate_unique(Pointer<sdJournal> j, Pointer<Pointer<?>> data,
                                                  Pointer<SizeT> l) {
        return sd_journal_enumerate_unique(Pointer.getPeer(j), Pointer.getPeer(data), Pointer.getPeer(l));
    }

    protected native static int sd_journal_enumerate_unique(@Ptr long j, @Ptr long data, @Ptr long l);

    public static void sd_journal_restart_unique(Pointer<sdJournal> j) {
        sd_journal_restart_unique(Pointer.getPeer(j));
    }

    protected native static void sd_journal_restart_unique(@Ptr long j);

    public static int sd_journal_get_fd(Pointer<sdJournal> j) {
        return sd_journal_get_fd(Pointer.getPeer(j));
    }

    protected native static int sd_journal_get_fd(@Ptr long j);

    public static int sd_journal_get_events(Pointer<sdJournal> j) {
        return sd_journal_get_events(Pointer.getPeer(j));
    }

    protected native static int sd_journal_get_events(@Ptr long j);

    public static int sd_journal_get_timeout(Pointer<sdJournal> j, Pointer<Long> timeout_usec) {
        return sd_journal_get_timeout(Pointer.getPeer(j), Pointer.getPeer(timeout_usec));
    }

    protected native static int sd_journal_get_timeout(@Ptr long j, @Ptr long timeout_usec);

    public static int sd_journal_process(Pointer<sdJournal> j) {
        return sd_journal_process(Pointer.getPeer(j));
    }

    protected native static int sd_journal_process(@Ptr long j);

    public static int sd_journal_wait(Pointer<sdJournal> j, long timeout_usec) {
        return sd_journal_wait(Pointer.getPeer(j), timeout_usec);
    }

    protected native static int sd_journal_wait(@Ptr long j, long timeout_usec);

    public static int sd_journal_reliable_fd(Pointer<sdJournal> j) {
        return sd_journal_reliable_fd(Pointer.getPeer(j));
    }

    protected native static int sd_journal_reliable_fd(@Ptr long j);

    public static int sd_journal_get_catalog(Pointer<sdJournal> j, Pointer<Pointer<Byte>> text) {
        return sd_journal_get_catalog(Pointer.getPeer(j), Pointer.getPeer(text));
    }

    protected native static int sd_journal_get_catalog(@Ptr long j, @Ptr long text);

    public static int sd_journal_get_catalog_for_message_id(sd_id128 id, Pointer<Pointer<Byte>> ret) {
        return sd_journal_get_catalog_for_message_id(id, Pointer.getPeer(ret));
    }

    protected native static int sd_journal_get_catalog_for_message_id(sd_id128 id, @Ptr long ret);

    public interface sdJournal {

    }

}
