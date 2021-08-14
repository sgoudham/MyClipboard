package me.goudham;

class SystemUtils {
    boolean isMac() {
        return org.apache.commons.lang3.SystemUtils.IS_OS_MAC;
    }

    boolean isUnix() {
        return org.apache.commons.lang3.SystemUtils.IS_OS_UNIX || org.apache.commons.lang3.SystemUtils.IS_OS_LINUX;
    }

    boolean isWindows() {
        return org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;
    }
}
