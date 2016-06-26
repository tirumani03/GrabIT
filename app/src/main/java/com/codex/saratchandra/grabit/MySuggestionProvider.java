package com.codex.saratchandra.grabit;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by SaratChandra on 6/21/2016.
 */

public class MySuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.codex.saratchandra.grabit.MySuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES| DATABASE_MODE_2LINES;

    public MySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}