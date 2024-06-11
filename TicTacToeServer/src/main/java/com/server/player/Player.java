package com.server.player;

import com.server.game.gameutils.Symbol;

import java.io.BufferedReader;
import java.io.PrintWriter;

public record Player(BufferedReader reader, PrintWriter writer, Symbol symbol)
{}