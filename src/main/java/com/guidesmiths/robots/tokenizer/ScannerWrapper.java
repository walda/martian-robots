package com.guidesmiths.robots.tokenizer;

import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class ScannerWrapper {

    private Scanner scanner = new Scanner(System.in);

    public boolean hasNext() {
        return scanner.hasNext();
    }

    public String nextLine() {
        return scanner.nextLine();
    }

}
