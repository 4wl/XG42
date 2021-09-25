package com.unknown.xg42.command.syntax.parsers;

import com.unknown.xg42.command.syntax.SyntaxChunk;
import com.unknown.xg42.module.IModule;
import com.unknown.xg42.module.ModuleManager;

public class ModuleParser extends AbstractParser {

    @Override
    public String getChunk(SyntaxChunk[] chunks, SyntaxChunk thisChunk, String[] values, String chunkValue) {
        if (chunkValue == null)
            return getDefaultChunk(thisChunk);

        IModule chosen = ModuleManager.getModules().stream()
                .filter(module -> module.getName().toLowerCase().startsWith(chunkValue.toLowerCase()))
                .findFirst()
                .orElse(null);
        if (chosen == null) return null;
        return chosen.getName().substring(chunkValue.length());
    }

}
