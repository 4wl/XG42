package com.unknown.xg42.command.syntax;

public interface SyntaxParser {
    String getChunk(SyntaxChunk[] chunks, SyntaxChunk thisChunk, String[] values, String chunkValue);
}