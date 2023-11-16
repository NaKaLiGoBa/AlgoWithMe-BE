package com.nakaligoba.backend.domain;

public enum Assistant {
    CODE_OPTIMIZER("asst_pFZ1B5NmD4QcGVureOYq5SRG"),
    ALGORITHM_SELECTOR("asst_p9uEmI0HRD6plGgguE3QUTxq"),
    EDGE_CASE_FINDER("asst_C8QYZdKqU2Png6QMIhBZhVyR");

    private final String id;

    Assistant(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
