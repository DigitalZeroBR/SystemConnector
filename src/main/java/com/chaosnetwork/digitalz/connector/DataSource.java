package com.chaosnetwork.digitalz.connector;

import com.google.common.base.Preconditions;

class DataSource {

    private final String sourceName;
    private final String username;
    private final String password;
    private final String url;

    public DataSource(String sourceName, String username, String password, String url) {
        this.sourceName = Preconditions.checkNotNull(sourceName, "sourceName").toLowerCase();
        this.username = Preconditions.checkNotNull(username, "username");
        this.password = Preconditions.checkNotNull(password, "password");
        this.url = Preconditions.checkNotNull(url, "url");
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "{sourceName: '" + sourceName + "', username: '" + username + "', url: '" + url + "'}";
    }
}
