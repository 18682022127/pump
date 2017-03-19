package com.itouch8.pump.core.util.parser;

import com.itouch8.pump.core.util.parser.impl.ParserComponentFactory;


public class ParserComponents {

    private static IParserComponentFactory factory = new ParserComponentFactory();

    
    public static IParserComponentFactory getFactory() {
        return factory;
    }

    
    public void setFactory(IParserComponentFactory factory) {
        if (null != factory) {
            ParserComponents.factory = factory;
        }
    }
}
