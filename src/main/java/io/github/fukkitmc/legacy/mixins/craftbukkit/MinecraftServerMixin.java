package io.github.fukkitmc.legacy.mixins.craftbukkit;

import io.github.fukkitmc.legacy.CursedOptionLoader;
import io.github.fukkitmc.legacy.extra.*;
import jline.console.ConsoleReader;
import joptsimple.OptionSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Bootstrap;
import net.minecraft.class_1626;
import net.minecraft.class_635;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.server.*;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Tickable;
import net.minecraft.util.UserCache;
import net.minecraft.util.Util;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.snooper.Snooper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.LevelGeneratorType;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.plugin.PluginLoadOrder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.FutureTask;

import static net.minecraft.server.MinecraftServer.*;

@Mixin(value = MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Shadow
    public int G;

    @Shadow
    public UserCache Z;

    @Shadow
    public PlayerManager v;

    @Shadow
    public CraftServer server;

    @Shadow
    public ConsoleReader reader;

    @Shadow
    public Profiler methodProfiler;
    @Shadow
    public long ab;
    @Shadow
    public ServerMetadata r;
    @Shadow
    public boolean w;
    @Shadow
    public long R;
    @Shadow
    public boolean Q;
    @Shadow
    public boolean x;
    @Shadow
    public String E;
    @Shadow
    @Final
    public Profiler c;
    @Shadow
    public long[][] i;
    @Shadow
    public int y;
    @Shadow
    public ServerWorld[] d;
    @Shadow
    public Queue<FutureTask<?>> j;
    @Shadow
    public List<Tickable> p;
    @Shadow
    public Queue<Runnable> processQueue;
    @Shadow
    public String O;
    @Shadow
    public String P;
    @Shadow
    public Snooper n;
    @Shadow
    public boolean N;

    /**
     * @author fukkit
     */
    @Environment(EnvType.SERVER)
    @Overwrite
    public static void main(String[] args) {
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
        OptionSet options = CursedOptionLoader.loadOptions(args);
        Bootstrap.initialize();
        String string2 = ".";

        MinecraftDedicatedServer dedicatedServer = new MinecraftDedicatedServer(new File(string2));

//        if (options.has("port")) {
//            int port = (Integer) options.valueOf("port");
//            if (port > 0) {
//                dedicatedserver.setPort(port);
//            }
//        }

        dedicatedServer.D();
        Runtime.getRuntime().addShutdownHook(new Thread(((MinecraftServerMixin) (Object) dedicatedServer)::t, "Server Shutdown Thread"));
        assert options != null;
        if (options.has("universe")) {
            dedicatedServer.universe = (File) options.valueOf("universe");
        }

//        if (options.has("world")) {
//            dedicatedserver.setWorld((String) options.valueOf("world"));
//        }

    }

    @Shadow
    public abstract void b(String string);

    @Shadow
    public abstract boolean X();

    @Shadow
    public abstract void s();

    @Shadow
    public abstract void a_(String string, int i);

    @Shadow
    public abstract boolean v();

    @Shadow
    public abstract boolean i() throws IOException;

    @Shadow
    public abstract void a(ServerMetadata serverPing);

    @Shadow
    public abstract void A();

    @Shadow
    public abstract void a(CrashReport crashReport);

    @Shadow
    public abstract CrashReport b(CrashReport crashReport);

    @Shadow
    public abstract File y();

    @Shadow
    public abstract void z();

    @Shadow
    public abstract boolean C();

    @Shadow
    public abstract ServerNetworkIo aq();

    @Shadow
    public abstract void a(boolean bl);

    @Shadow public List<ServerWorld> worlds;

    @Inject(method = "<init>(Ljava/io/File;Ljava/net/Proxy;Ljava/io/File;)V", at = @At("TAIL"))
    public void constructor(File file, Proxy proxy, File file2, CallbackInfo ci) {
        try {
            methodProfiler = new Profiler();
            processQueue = new java.util.concurrent.ConcurrentLinkedQueue<>();
            ((MinecraftServer)(Object)this).worlds = new ArrayList<>();
            ((MinecraftServer) (Object) this).options = CursedOptionLoader.bukkitOptions;
            this.reader = new ConsoleReader(System.in, System.out);
            if (System.console() == null && System.getProperty("jline.terminal") == null) {
                System.setProperty("jline.terminal", "jline.UnsupportedTerminal");
            }
            LOGGER = LOGGER;
        } catch (Exception exception) {
            throw new RuntimeException("Fukkit died CRAB", exception);
        }
    }

    @Inject(method = "O", at = @At("HEAD"), cancellable = true)
    public void o(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    /**
     * @author fukkit
     */
    @Overwrite
    public String getServerModName() {
        return server.getName();
    }

    /**
     * @author fukkit
     */
    @Overwrite
    public ServerWorld a(int i) {
        if (i == -1) {
            return ((MinecraftServer)(Object)this).worlds.get(1);
        } else {
            return i == 1 ? ((MinecraftServer)(Object)this).worlds.get(2) : ((MinecraftServer)(Object)this).worlds.get(0);
        }
    }

    /**
     * @author fukkit
     */
    @Overwrite
    public void B() {
        this.methodProfiler.push("jobs");
        Queue queue = this.j;

        synchronized (this.j) {
            while (!this.j.isEmpty()) {
                Util.a((FutureTask) this.j.poll(), MinecraftServer.LOGGER);
            }
        }

        this.methodProfiler.swap("levels");

        // CraftBukkit start
        this.server.getScheduler().mainThreadHeartbeat(this.y);

        // Run tasks that are waiting on processing
        while (!processQueue.isEmpty()) {
            processQueue.remove().run();
        }

        org.bukkit.craftbukkit.chunkio.ChunkIOExecutor.tick();

        // Send time updates to everyone, it will get the right time from the world the player is in.
        if (this.y % 20 == 0) {
            for (int i = 0; i < ((MinecraftServerExtra)this).getPlayerList().players.size(); ++i) {
                ServerPlayerEntity entityplayer = (ServerPlayerEntity) ((MinecraftServerExtra)this).getPlayerList().players.get(i);
                entityplayer.playerConnection.sendPacket(new WorldTimeUpdateS2CPacket(entityplayer.world.getTime(), ((EntityPlayerExtra)entityplayer).getPlayerTime(), entityplayer.world.getGameRules().getBoolean("doDaylightCycle"))); // Add support for per player time
            }
        }

        int i;

        for (i = 0; i < this.worlds.size(); ++i) {
            long j = System.nanoTime();

            // if (i == 0 || this.getAllowNether()) {
            ServerWorld worldserver = this.worlds.get(i);

            this.methodProfiler.push(worldserver.getWorldData().getName());
                /* Drop global time updates
                if (this.ticks % 20 == 0) {
                    this.methodProfiler.a("timeSync");
                    this.v.a(new PacketPlayOutUpdateTime(worldserver.getTime(), worldserver.getDayTime(), worldserver.getGameRules().getBoolean("doDaylightCycle")), worldserver.worldProvider.getDimension());
                    this.methodProfiler.b();
                }
                // CraftBukkit end */

            this.methodProfiler.push("tick");

            CrashReport crashreport;

            try {
                worldserver.tick();
            } catch (Throwable throwable) {
                crashreport = CrashReport.create(throwable, "Exception ticking world");
                worldserver.a(crashreport);
                throw new CrashException(crashreport);
            }

            try {
                worldserver.tickEntities();
            } catch (Throwable throwable1) {
                crashreport = CrashReport.create(throwable1, "Exception ticking world entities");
                worldserver.a(crashreport);
                throw new CrashException(crashreport);
            }

            this.methodProfiler.pop();
            this.methodProfiler.push("tracker");
            worldserver.getTracker().updatePlayers();
            this.methodProfiler.pop();
            this.methodProfiler.pop();
            // } // CraftBukkit

            // this.i[i][this.ticks % 100] = System.nanoTime() - j; // CraftBukkit
        }

        this.methodProfiler.swap("connection");
        this.aq().tick();
        this.methodProfiler.swap("players");
        this.v.updatePlayerLatency();
        this.methodProfiler.swap("tickables");

        for (i = 0; i < this.p.size(); ++i) {
            this.p.get(i).tick();
        }

        this.methodProfiler.pop();
    }

    /**
     * @author fukkit
     */
    @Overwrite
    public void t() {
        if (!this.N) {
            LOGGER.info("Stopping server");
            if (this.aq() != null) {
                this.aq().stop();
            }

            if (this.v != null) {
                LOGGER.info("Saving players");
                this.v.saveAllPlayerData();
                this.v.disconnectAllPlayers();
            }

            if (this.d != null) {
                LOGGER.info("Saving worlds");
                this.a(false);

                for (ServerWorld worldServer : ((MinecraftServer)(Object)this).worlds) {
                    worldServer.saveLevel();
                }
            }

            if (this.n.d()) {
                this.n.e();
            }

        }
    }

    /**
     * @author fukkit
     */
    @Overwrite
    public void run() {
        try {
            if (this.i()) {
                this.ab = getTimeMillis();
                long l = 0L;
                this.r.setDescription(new LiteralText(this.E));
                this.r.setVersion(new ServerMetadata.Version("1.8.9", 47));
                this.a(this.r);

                while (this.w) {
                    long m = getTimeMillis();
                    long n = m - this.ab;
                    if (n > 2000L && this.ab - this.R >= 15000L) {
                        LOGGER.warn("Can't keep up! Did the system time change, or is the server overloaded? Running {}ms behind, skipping {} tick(s)", new Object[]{n, n / 50L});
                        n = 2000L;
                        this.R = this.ab;
                    }

                    if (n < 0L) {
                        LOGGER.warn("Time ran backwards! Did the system time change?");
                        n = 0L;
                    }

                    l += n;
                    this.ab = m;
                    if (((MinecraftServer) (Object) this).worlds.get(0).isReady()) {
                        this.A();
                        l = 0L;
                    } else {
                        while (l > 50L) {
                            l -= 50L;
                            this.A();
                        }
                    }

                    Thread.sleep(Math.max(1L, 50L - l));
                    this.Q = true;
                }
            } else {
                this.a((CrashReport) null);
            }
        } catch (Throwable var46) {
            LOGGER.error("Encountered an unexpected exception", var46);
            CrashReport crashReport = null;
            if (var46 instanceof CrashException) {
                crashReport = this.b(((CrashException) var46).getReport());
            } else {
                crashReport = this.b(new CrashReport("Exception in server tick loop", var46));
            }

            File file = new File(new File(this.y(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");
            if (crashReport.writeToFile(file)) {
                LOGGER.error("This crash report has been saved to: " + file.getAbsolutePath());
            } else {
                LOGGER.error("We were unable to save this crash report to disk.");
            }

            this.a(crashReport);
        } finally {
            try {
                this.x = true;
                this.t();
            } catch (Throwable var44) {
                LOGGER.error("Exception stopping the server", var44);
            } finally {
                this.z();
            }

        }

    }


    @Inject(method = "s", at = @At("TAIL"))
    public void enablePluginsPostWorld(CallbackInfo ci) {
        ((MinecraftServer) (Object) this).server.enablePlugins(PluginLoadOrder.POSTWORLD);
    }

    /**
     * @author fukkit
     */
    @Overwrite(remap = false)
    public void a(String s, String s1, long i, LevelGeneratorType worldtype, String s2) {
        ((MinecraftServer) (Object) this).upgradeWorld(s);
        this.b("menu.loadingLevel");
        this.d = new ServerWorld[3];
        int worldCount = 3;

        for (int j = 0; j < worldCount; ++j) {
            ServerWorld world;
            byte dimension = 0;

            if (j == 1) {
                if (((MinecraftServerExtra)this).getAllowNether()) {
                    dimension = -1;
                } else {
                    continue;
                }
            }

            if (j == 2) {
                if (server.getAllowEnd()) {
                    dimension = 1;
                } else {
                    continue;
                }
            }

            String worldType = org.bukkit.World.Environment.getEnvironment(dimension).toString().toLowerCase();
            String name = (dimension == 0) ? s : s + "_" + worldType;

            org.bukkit.generator.ChunkGenerator gen = this.server.getGenerator(name);
            LevelInfo worldsettings = new LevelInfo(i, ((MinecraftServerExtra)this).getGamemode(), ((MinecraftServerExtra)this).getGenerateStructures(), ((MinecraftServerExtra)this).isHardcore(), worldtype);
            worldsettings.setGeneratorOptions(s2);

            if (j == 0) {
                class_635 idatamanager = new ServerNBTManager(server.getWorldContainer(), s1, true);
                LevelProperties worlddata = idatamanager.getLevelProperties();
                if (worlddata == null) {
                    worlddata = new LevelProperties(worldsettings, s1);
                }
                ((WorldDataExtra)worlddata).checkName(s1); // CraftBukkit - Migration did not rewrite the level.dat; This forces 1.8 to take the last loaded world as respawn (in this case the end)
                if (this.X()) {
                    world = (ServerWorld) (new class_1626(((MinecraftServer) (Object) this), idatamanager, worlddata, dimension, this.methodProfiler)).getWorld();
                } else {
                    ServerWorld sworld = new ServerWorld(((MinecraftServer) (Object) this), idatamanager, worlddata, dimension, this.methodProfiler);
                    ((WorldExtra)sworld).bukkitInit(gen, org.bukkit.World.Environment.getEnvironment(dimension));
                    world = (ServerWorld) (sworld).getWorld();

                }

                world.a(worldsettings);
                this.server.scoreboardManager = new org.bukkit.craftbukkit.scoreboard.CraftScoreboardManager(((MinecraftServer) (Object) this), world.getScoreboard());
            } else {
                String dim = "DIM" + dimension;

                File newWorld = new File(new File(name), dim);
                File oldWorld = new File(new File(s), dim);

                if ((!newWorld.isDirectory()) && (oldWorld.isDirectory())) {
                    MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder required ----");
                    MinecraftServer.LOGGER.info("Unfortunately due to the way that Minecraft implemented multiworld support in 1.6, Bukkit requires that you move your " + worldType + " folder to a new location in order to operate correctly.");
                    MinecraftServer.LOGGER.info("We will move this folder for you, but it will mean that you need to move it back should you wish to stop using Bukkit in the future.");
                    MinecraftServer.LOGGER.info("Attempting to move " + oldWorld + " to " + newWorld + "...");

                    if (newWorld.exists()) {
                        MinecraftServer.LOGGER.warn("A file or folder already exists at " + newWorld + "!");
                        MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
                    } else if (newWorld.getParentFile().mkdirs()) {
                        if (oldWorld.renameTo(newWorld)) {
                            MinecraftServer.LOGGER.info("Success! To restore " + worldType + " in the future, simply move " + newWorld + " to " + oldWorld);
                            // Migrate world data too.
                            try {
                                com.google.common.io.Files.copy(new File(new File(s), "level.dat"), new File(new File(name), "level.dat"));
                            } catch (IOException exception) {
                                MinecraftServer.LOGGER.warn("Unable to migrate world data.");
                            }
                            MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder complete ----");
                        } else {
                            MinecraftServer.LOGGER.warn("Could not move folder " + oldWorld + " to " + newWorld + "!");
                            MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
                        }
                    } else {
                        MinecraftServer.LOGGER.warn("Could not create path for " + newWorld + "!");
                        MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
                    }
                }

                class_635 idatamanager = new ServerNBTManager(server.getWorldContainer(), name, true);
                // world =, b0 to dimension, s1 to name, added Environment and gen
                LevelProperties worlddata = idatamanager.getLevelProperties();
                if (worlddata == null) {
                    worlddata = new LevelProperties(worldsettings, name);
                }
                ((WorldDataExtra)worlddata).checkName(name); // CraftBukkit - Migration did not rewrite the level.dat; This forces 1.8 to take the last loaded world as respawn (in this case the end)
                ServerWorld wsrver = new SecondaryWorldServer(((MinecraftServer) (Object) this), idatamanager, dimension, ((MinecraftServer)(Object)this).worlds.get(0), this.methodProfiler);
                ((WorldExtra)wsrver).bukkitInit(gen, org.bukkit.World.Environment.getEnvironment(dimension));
                world = (ServerWorld) wsrver.getWorld();
            }

            this.server.getPluginManager().callEvent(new org.bukkit.event.world.WorldInitEvent(((WorldExtra)world).getWorld()));

            world.addIWorldAccess(new WorldManager(((MinecraftServer) (Object) this), world));
            if (!((MinecraftServer) (Object) this).isSinglePlayer()) {
                world.getWorldData().setGameType(((MinecraftServerExtra)this).getGamemode());
            }

            ((MinecraftServer) (Object) this).worlds.add(world);
            ((PlayerListExtra)((MinecraftServerExtra)this).getPlayerList()).setPlayerFileData(((MinecraftServer)(Object)this).worlds.toArray(new ServerWorld[((MinecraftServer)(Object)this).worlds.size()]));
        }

        // CraftBukkit end
        this.a((((MinecraftServerExtra)this)).getDifficulty());
        this.k();
    }

    /**
     * @author fukkit
     */
    @Overwrite
    public void a(Difficulty enumdifficulty) {
        // CraftBukkit start
        for (ServerWorld worldserver : ((MinecraftServer)(Object)this).worlds) {
            // CraftBukkit end

            if (worldserver != null) {
                if (worldserver.getWorldData().isHardcore()) {
                    worldserver.getWorldData().setDifficulty(Difficulty.HARD);
                    worldserver.setSpawnFlags(true, true);
                } else if (((MinecraftServer) (Object) this).isSinglePlayer()) {
                    worldserver.getWorldData().setDifficulty(enumdifficulty);
                    worldserver.setSpawnFlags(worldserver.getDifficulty() != Difficulty.PEACEFUL, true);
                } else {
                    worldserver.getWorldData().setDifficulty(enumdifficulty);
                    worldserver.setSpawnFlags(((MinecraftServerExtra) this).getSpawnMonsters(), ((MinecraftServer) (Object) this).spawnAnimals);
                }
            }
        }

    }

    /**
     * @author fukkit
     */
    @Overwrite
    public void method_6511() {
        int i = 0;
        this.b("menu.generatingTerrain");
        // CraftBukkit start - fire WorldLoadEvent and handle whether or not to keep the spawn in memory
        for (int m = 0; m < ((MinecraftServer)(Object)this).worlds.size(); m++) {
            ServerWorld worldserver = ((MinecraftServer)(Object)this).worlds.get(m);
            LOGGER.info("Preparing start region for level " + m + " (Seed: " + worldserver.getSeed() + ")");

            if (!((WorldExtra)worldserver).getWorld().getKeepSpawnInMemory()) {
                continue;
            }

            BlockPos blockposition = worldserver.getSpawn();
            long j = getTimeMillis();
            i = 0;

            for (int k = -192; k <= 192 && ((MinecraftServerExtra)this).isRunning(); k += 16) {
                for (int l = -192; l <= 192 && ((MinecraftServerExtra)this).isRunning(); l += 16) {
                    long i1 = getTimeMillis();

                    if (i1 - j > 1000L) {
                        this.a_("Preparing spawn area", i * 100 / 625);
                        j = i1;
                    }

                    ++i;
                    worldserver.chunkProviderServer.getChunkAt(blockposition.getX() + k >> 4, blockposition.getZ() + l >> 4);
                }
            }
        }

        for (ServerWorld world : ((MinecraftServer)(Object)this).worlds) {
            this.server.getPluginManager().callEvent(new org.bukkit.event.world.WorldLoadEvent(((WorldExtra)world).getWorld()));
        }
        // CraftBukkit end
        this.s();
    }

    /**
     * @author fukkit
     * @funcName tabCompleteCommand
     */
    @Overwrite
    public List<String> a(CommandSource icommandlistener, String s, BlockPos blockposition) {
        return server.tabComplete(icommandlistener, s);
        // CraftBukkit end
    }

}
