package com.prove.prove;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

public class ModulitTests {
    @Test
    void verifyModuleStructure() {
        ApplicationModules modules = ApplicationModules.of(ProveApplication.class);
        modules.verify();
    }
}
