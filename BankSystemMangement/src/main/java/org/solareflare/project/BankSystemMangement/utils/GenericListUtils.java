package org.solareflare.project.BankSystemMangement.utils;

import java.util.ArrayList;
import java.util.List;

public class GenericListUtils {

    public static <T> List<T> initializeListIfEmpty(List<T> list) {
        return list.isEmpty() ? new ArrayList<>() : list;
    }
}
