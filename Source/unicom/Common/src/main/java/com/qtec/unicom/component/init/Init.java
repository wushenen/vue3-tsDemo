package com.qtec.unicom.component.init;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class Init {

    public static Set<String> IP_WHITE_SET = new CopyOnWriteArraySet();

}


