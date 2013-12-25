package org.lantern.util;

import java.util.HashMap;

import org.lantern.LanternConstants;
import org.lantern.LanternModule;
import org.lantern.LanternService;
import org.lantern.Launcher;
import org.lantern.state.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.iterative.groovy.service.GroovyShellService;

/**
 * <p>
 * A shell server that allows remote access to the Lantern system via socat.
 * </p>
 * 
 * <p>
 * Uses <a
 * href="https://github.com/bazhenov/groovy-shell-server">groovy-shell-server
 * </a>.
 * </p>
 */
@Singleton
public class ShellServer implements LanternService {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ShellServer.class);

    private final GroovyShellService service;

    @Inject
    public ShellServer(final Model model) {
        this.service = new GroovyShellService();
        service.setPort(LanternConstants.LANTERN_LOCALHOST_HTTP_PORT + 1);
        service.setBindings(new HashMap<String, Object>() {
            {
                put("model", model);
            }
        });
    }

    public void setLantern(LanternModule lantern) {
        service.getBindings().put("lantern", lantern);
    }

    public void setLauncher(Launcher launcher) {
        service.getBindings().put("launcher", launcher);
    }

    @Override
    public void start() {
        try {
            service.start();
        } catch (Exception e) {
            LOGGER.error("Unable to start shell server", e);
        }
    }

    @Override
    public void stop() {
        try {
            service.destroy();
        } catch (InterruptedException ie) {
            LOGGER.warn("Unable to stop shell server.");
        }
    }
}
