package io.github.fukkitmc.fukkit.log;

import net.minecraft.server.MinecraftServer;

import java.util.ResourceBundle;
import java.util.function.Supplier;
import java.util.logging.*;

public class FukkitLogger extends Logger {
    
    public Logger originalLogger;
    
    public FukkitLogger(Logger minecraft) {
        super(minecraft.getName(), minecraft.getResourceBundleName());
        originalLogger = minecraft;
    }

    @Override
    public void info(String msg) {
        MinecraftServer.LOGGER.info(msg);
    }

    @Override
    public ResourceBundle getResourceBundle() {
        return originalLogger.getResourceBundle();
    }

    @Override
    public String getResourceBundleName() {
        return originalLogger.getResourceBundleName();
    }

    @Override
    public void setFilter(Filter newFilter) throws SecurityException {
        originalLogger.setFilter(newFilter);
    }

    @Override
    public Filter getFilter() {
        return originalLogger.getFilter();
    }

    @Override
    public void log(LogRecord record) {
        originalLogger.log(record);
    }

    @Override
    public void log(Level level, String msg) {
        if(level == Level.INFO){
            MinecraftServer.LOGGER.info(msg);
        }else if(level == Level.WARNING){
            MinecraftServer.LOGGER.warn(msg);
        }else if(level == Level.SEVERE){
            MinecraftServer.LOGGER.fatal(msg);
        }else {
            System.out.println("uuh... ohno");
        }
    }

    @Override
    public void log(Level level, Supplier<String> msgSupplier) {
        originalLogger.log(level, msgSupplier);
    }

    @Override
    public void log(Level level, String msg, Object param1) {
        originalLogger.log(level, msg, param1);
    }

    @Override
    public void log(Level level, String msg, Object[] params) {
        originalLogger.log(level, msg, params);
    }

    @Override
    public void log(Level level, String msg, Throwable thrown) {
        log(level, msg);
        MinecraftServer.LOGGER.fatal(thrown);
        thrown.printStackTrace();
    }

    @Override
    public void log(Level level, Throwable thrown, Supplier<String> msgSupplier) {
        originalLogger.log(level, thrown, msgSupplier);
    }

    @Override
    public void throwing(String sourceClass, String sourceMethod, Throwable thrown) {
        originalLogger.throwing(sourceClass, sourceMethod, thrown);
    }

    @Override
    public void severe(String msg) {
        originalLogger.severe(msg);
    }

    @Override
    public void warning(String msg) {
        originalLogger.warning(msg);
    }

    @Override
    public void config(String msg) {
        originalLogger.config(msg);
    }

    @Override
    public void fine(String msg) {
        originalLogger.fine(msg);
    }

    @Override
    public void finer(String msg) {
        originalLogger.finer(msg);
    }

    @Override
    public void finest(String msg) {
        originalLogger.finest(msg);
    }

    @Override
    public void severe(Supplier<String> msgSupplier) {
        originalLogger.severe(msgSupplier);
    }

    @Override
    public void warning(Supplier<String> msgSupplier) {
        originalLogger.warning(msgSupplier);
    }

    @Override
    public void info(Supplier<String> msgSupplier) {
        originalLogger.info(msgSupplier);
    }

    @Override
    public void config(Supplier<String> msgSupplier) {
        originalLogger.config(msgSupplier);
    }

    @Override
    public void fine(Supplier<String> msgSupplier) {
        originalLogger.fine(msgSupplier);
    }

    @Override
    public void finer(Supplier<String> msgSupplier) {
        originalLogger.finer(msgSupplier);
    }

    @Override
    public void finest(Supplier<String> msgSupplier) {
        originalLogger.finest(msgSupplier);
    }

    @Override
    public void setLevel(Level newLevel) throws SecurityException {
        originalLogger.setLevel(newLevel);
    }

    @Override
    public Level getLevel() {
        return originalLogger.getLevel();
    }

    @Override
    public boolean isLoggable(Level level) {
        return originalLogger.isLoggable(level);
    }

    @Override
    public String getName() {
        return originalLogger.getName();
    }

    @Override
    public void addHandler(Handler handler) throws SecurityException {
        originalLogger.addHandler(handler);
    }

    @Override
    public void removeHandler(Handler handler) throws SecurityException {
        originalLogger.removeHandler(handler);
    }

    @Override
    public Handler[] getHandlers() {
        return originalLogger.getHandlers();
    }

    @Override
    public void setUseParentHandlers(boolean useParentHandlers) {
        originalLogger.setUseParentHandlers(useParentHandlers);
    }

    @Override
    public boolean getUseParentHandlers() {
        return originalLogger.getUseParentHandlers();
    }

    @Override
    public void setResourceBundle(ResourceBundle bundle) {
        originalLogger.setResourceBundle(bundle);
    }

    @Override
    public Logger getParent() {
        return originalLogger.getParent();
    }

    @Override
    public void setParent(Logger parent) {
        originalLogger.setParent(parent);
    }
}
