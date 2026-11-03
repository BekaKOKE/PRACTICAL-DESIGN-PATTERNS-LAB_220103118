package task3;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Task3Test {
    static int passed = 0;
    static int failed = 0;

    static void check(boolean condition, String name) {
        if (condition) {
            passed++;
            System.out.println("[PASS] " + name);
        } else {
            failed++;
            System.out.println("[FAIL] " + name);
        }
    }

    static String capture(Runnable r) {
        PrintStream old = System.out;
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buf, true));
        try {
            r.run();
        } finally {
            System.setOut(old);
        }
        return buf.toString().replace("\r\n", "\n");
    }

    public static void main(String[] args) {
        DispatchStrategy business = new BusinessHoursStrategy();
        DispatchStrategy immediate = new ImmediateStrategy();

        check(!business.shouldDispatch(1, 8), "08:00 blocked");
        check(business.shouldDispatch(1, 9), "09:00 allowed");
        check(business.shouldDispatch(1, 17), "17:00 allowed");
        check(!business.shouldDispatch(1, 18), "18:00 blocked");
        check(!business.shouldDispatch(4, 2), "severity 4 at night blocked");
        check(business.shouldDispatch(5, 2), "severity 5 bypasses hours");
        check(immediate.shouldDispatch(1, 3), "immediate always dispatches");

        String sms = capture(() -> new CriticalSystemAlert(new SmsChannel(), business).processAlert("+1", "disk full", 2));
        check(sms.equals("[SMS PROTOCOL -> +1] !!! CRITICAL PRODUCTION ALERT !!! -- Incident Report: disk full -- ACTION REQUIRED: SRE on-call immediate intervention.\n"), "critical alert over sms");

        String blocked = capture(() -> new DailySummaryAlert(new EmailChannel(), business).processAlert("a@b", "ok", 23));
        check(blocked.startsWith("[DISPATCH SUPPRESSED]") && blocked.contains("23:00 (Severity: 1)"), "daily summary suppressed at night");

        String night = capture(() -> new DailySummaryAlert(new EmailChannel(), immediate).processAlert("a@b", "ok", 23));
        check(night.equals("[EMAIL PROTOCOL -> a@b]\nDaily Digest:\nok\n"), "immediate strategy sends at night");

        String email = capture(() -> new CriticalSystemAlert(new EmailChannel(), immediate).processAlert("ops@x", "down", 4));
        check(email.startsWith("[EMAIL PROTOCOL -> ops@x]\n!!! CRITICAL PRODUCTION ALERT !!!\n"), "same alert works with other channel");

        String direct = capture(() -> new DailySummaryAlert(new SmsChannel(), business).send("+2", "late", 7, 3));
        check(direct.startsWith("[SMS PROTOCOL -> +2] Daily Digest: -- late"), "send with explicit severity");

        boolean thrown = false;
        try {
            new DailySummaryAlert(null, business);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        check(thrown, "null channel rejected");

        System.out.println("Task3: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
