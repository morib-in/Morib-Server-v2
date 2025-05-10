package org.morib.server.global.common;

public record S3Info(
        String type,
        String url
) {
    public static S3Info of (String type, String url) {
        return new S3Info(type, url);
    }
}
