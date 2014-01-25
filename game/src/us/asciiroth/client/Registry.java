/**
 * Copyright 2008 Alx Dark
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.    
 */
package us.asciiroth.client;

import static us.asciiroth.client.core.Flags.NOT_EDITABLE;

import java.util.HashMap;
import java.util.Map;

import us.asciiroth.client.agents.Asciiroth;
import us.asciiroth.client.agents.Boulder;
import us.asciiroth.client.agents.Campfire;
import us.asciiroth.client.agents.Cephalid;
import us.asciiroth.client.agents.Corvid;
import us.asciiroth.client.agents.Farthapod;
import us.asciiroth.client.agents.GreatOldOne;
import us.asciiroth.client.agents.Hooloovoo;
import us.asciiroth.client.agents.KillerBee;
import us.asciiroth.client.agents.LavaWorm;
import us.asciiroth.client.agents.LightningLizard;
import us.asciiroth.client.agents.Optilisk;
import us.asciiroth.client.agents.Paralyzed;
import us.asciiroth.client.agents.Pillar;
import us.asciiroth.client.agents.Pusher;
import us.asciiroth.client.agents.Rhindle;
import us.asciiroth.client.agents.RollingBoulder;
import us.asciiroth.client.agents.Sleestak;
import us.asciiroth.client.agents.Slider;
import us.asciiroth.client.agents.Statue;
import us.asciiroth.client.agents.Tetrite;
import us.asciiroth.client.agents.Thermadon;
import us.asciiroth.client.agents.Triffid;
import us.asciiroth.client.agents.Tumbleweed;
import us.asciiroth.client.agents.npc.Archer;
import us.asciiroth.client.agents.npc.Commoner;
import us.asciiroth.client.agents.npc.MallocArcher;
import us.asciiroth.client.agents.npc.MallocCommoner;
import us.asciiroth.client.agents.npc.MallocNoble;
import us.asciiroth.client.agents.npc.MallocRifleman;
import us.asciiroth.client.agents.npc.MallocWizard;
import us.asciiroth.client.agents.npc.Noble;
import us.asciiroth.client.agents.npc.Rifleman;
import us.asciiroth.client.agents.npc.Wizard;
import us.asciiroth.client.agents.trees.Alder;
import us.asciiroth.client.agents.trees.Cactus;
import us.asciiroth.client.agents.trees.Cypress;
import us.asciiroth.client.agents.trees.Elm;
import us.asciiroth.client.agents.trees.Fir;
import us.asciiroth.client.agents.trees.Hemlock;
import us.asciiroth.client.agents.trees.Maple;
import us.asciiroth.client.agents.trees.Oak;
import us.asciiroth.client.agents.trees.Spruce;
import us.asciiroth.client.agents.trees.Stump;
import us.asciiroth.client.agents.trees.Willow;
import us.asciiroth.client.core.Effect;
import us.asciiroth.client.core.Piece;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.effects.EnergyCloud;
import us.asciiroth.client.effects.Fire;
import us.asciiroth.client.effects.PoisonCloud;
import us.asciiroth.client.effects.ResistancesCloud;
import us.asciiroth.client.items.Agentray;
import us.asciiroth.client.items.AmmoBow;
import us.asciiroth.client.items.AmmoGun;
import us.asciiroth.client.items.AmmoParalyzer;
import us.asciiroth.client.items.AmmoSling;
import us.asciiroth.client.items.Apple;
import us.asciiroth.client.items.Arrow;
import us.asciiroth.client.items.BlueRing;
import us.asciiroth.client.items.Bomb;
import us.asciiroth.client.items.Bone;
import us.asciiroth.client.items.Bow;
import us.asciiroth.client.items.Bread;
import us.asciiroth.client.items.Bullet;
import us.asciiroth.client.items.Chalice;
import us.asciiroth.client.items.Chalk;
import us.asciiroth.client.items.CopperPill;
import us.asciiroth.client.items.Crowbar;
import us.asciiroth.client.items.Crystal;
import us.asciiroth.client.items.Dagger;
import us.asciiroth.client.items.EuclideanShard;
import us.asciiroth.client.items.Fireball;
import us.asciiroth.client.items.Fish;
import us.asciiroth.client.items.FishingPole;
import us.asciiroth.client.items.GlassEye;
import us.asciiroth.client.items.GoldCoin;
import us.asciiroth.client.items.GoldenHarp;
import us.asciiroth.client.items.Grenade;
import us.asciiroth.client.items.Gun;
import us.asciiroth.client.items.Hammer;
import us.asciiroth.client.items.Head;
import us.asciiroth.client.items.Healer;
import us.asciiroth.client.items.HelmOfTheAsciiroth;
import us.asciiroth.client.items.KelpSmoothie;
import us.asciiroth.client.items.Key;
import us.asciiroth.client.items.Kiwi;
import us.asciiroth.client.items.MirrorShield;
import us.asciiroth.client.items.Mushroom;
import us.asciiroth.client.items.Parabullet;
import us.asciiroth.client.items.Paralyzer;
import us.asciiroth.client.items.PeachElixir;
import us.asciiroth.client.items.PoisonDart;
import us.asciiroth.client.items.ProteinBar;
import us.asciiroth.client.items.PurpleMushroom;
import us.asciiroth.client.items.RedRing;
import us.asciiroth.client.items.Rock;
import us.asciiroth.client.items.Scroll;
import us.asciiroth.client.items.SilverAnkh;
import us.asciiroth.client.items.Sling;
import us.asciiroth.client.items.SlingRock;
import us.asciiroth.client.items.Stoneray;
import us.asciiroth.client.items.Sword;
import us.asciiroth.client.items.TerminusEst;
import us.asciiroth.client.items.Weakray;
import us.asciiroth.client.terrain.Altar;
import us.asciiroth.client.terrain.BeeHive;
import us.asciiroth.client.terrain.Boards;
import us.asciiroth.client.terrain.Bookshelf;
import us.asciiroth.client.terrain.Bridge;
import us.asciiroth.client.terrain.BubblingLava;
import us.asciiroth.client.terrain.Bushes;
import us.asciiroth.client.terrain.CaveEntrance;
import us.asciiroth.client.terrain.ChalkedFloor;
import us.asciiroth.client.terrain.Chest;
import us.asciiroth.client.terrain.Cliff;
import us.asciiroth.client.terrain.Cloud;
import us.asciiroth.client.terrain.Crate;
import us.asciiroth.client.terrain.Crevasse;
import us.asciiroth.client.terrain.Dirt;
import us.asciiroth.client.terrain.Door;
import us.asciiroth.client.terrain.EmptyChest;
import us.asciiroth.client.terrain.EuclideanEngine;
import us.asciiroth.client.terrain.EuclideanTransporter;
import us.asciiroth.client.terrain.Exchanger;
import us.asciiroth.client.terrain.FarthapodNest;
import us.asciiroth.client.terrain.Fence;
import us.asciiroth.client.terrain.Field;
import us.asciiroth.client.terrain.FishPool;
import us.asciiroth.client.terrain.Floor;
import us.asciiroth.client.terrain.Flowers;
import us.asciiroth.client.terrain.ForceField;
import us.asciiroth.client.terrain.Forest;
import us.asciiroth.client.terrain.Fountain;
import us.asciiroth.client.terrain.Gate;
import us.asciiroth.client.terrain.Haystack;
import us.asciiroth.client.terrain.HighRocks;
import us.asciiroth.client.terrain.ImpassableCliffs;
import us.asciiroth.client.terrain.KeySwitch;
import us.asciiroth.client.terrain.Lava;
import us.asciiroth.client.terrain.LowRocks;
import us.asciiroth.client.terrain.Mud;
import us.asciiroth.client.terrain.Ocean;
import us.asciiroth.client.terrain.Pier;
import us.asciiroth.client.terrain.Pit;
import us.asciiroth.client.terrain.PressurePlate;
import us.asciiroth.client.terrain.Pylon;
import us.asciiroth.client.terrain.PyramidWall;
import us.asciiroth.client.terrain.Raft;
import us.asciiroth.client.terrain.Reflector;
import us.asciiroth.client.terrain.Rubble;
import us.asciiroth.client.terrain.RustyGate;
import us.asciiroth.client.terrain.Sand;
import us.asciiroth.client.terrain.ShallowSwamp;
import us.asciiroth.client.terrain.ShallowWater;
import us.asciiroth.client.terrain.Shooter;
import us.asciiroth.client.terrain.Sign;
import us.asciiroth.client.terrain.Sky;
import us.asciiroth.client.terrain.StairsDown;
import us.asciiroth.client.terrain.StairsUp;
import us.asciiroth.client.terrain.Surf;
import us.asciiroth.client.terrain.Swamp;
import us.asciiroth.client.terrain.Switch;
import us.asciiroth.client.terrain.Teleporter;
import us.asciiroth.client.terrain.Throne;
import us.asciiroth.client.terrain.TrashPile;
import us.asciiroth.client.terrain.Turnstile;
import us.asciiroth.client.terrain.Urn;
import us.asciiroth.client.terrain.VendingMachine;
import us.asciiroth.client.terrain.Wall;
import us.asciiroth.client.terrain.Water;
import us.asciiroth.client.terrain.Waterfall;
import us.asciiroth.client.terrain.Weeds;
import us.asciiroth.client.terrain.WishingWell;
import us.asciiroth.client.terrain.WoodPiling;
import us.asciiroth.client.terrain.decorators.AgentDestroyer;
import us.asciiroth.client.terrain.decorators.AgentGate;
import us.asciiroth.client.terrain.decorators.ColorRelay;
import us.asciiroth.client.terrain.decorators.DualTerrain;
import us.asciiroth.client.terrain.decorators.EnergyTrapContainer;
import us.asciiroth.client.terrain.decorators.Equipper;
import us.asciiroth.client.terrain.decorators.Flagger;
import us.asciiroth.client.terrain.decorators.Messenger;
import us.asciiroth.client.terrain.decorators.Mimic;
import us.asciiroth.client.terrain.decorators.PieceCreator;
import us.asciiroth.client.terrain.decorators.PitTrap;
import us.asciiroth.client.terrain.decorators.PlayerGate;
import us.asciiroth.client.terrain.decorators.PoisonTrapContainer;
import us.asciiroth.client.terrain.decorators.ResistancesTrapContainer;
import us.asciiroth.client.terrain.decorators.SecretPassage;
import us.asciiroth.client.terrain.decorators.Timer;
import us.asciiroth.client.terrain.decorators.Unequipper;
import us.asciiroth.client.terrain.decorators.Unflagger;
import us.asciiroth.client.terrain.decorators.WinGame;
import us.asciiroth.client.terrain.grasses.BeachGrass;
import us.asciiroth.client.terrain.grasses.BunchGrass;
import us.asciiroth.client.terrain.grasses.Grass;
import us.asciiroth.client.terrain.grasses.Scrub;
import us.asciiroth.client.terrain.grasses.SwampGrass;
import us.asciiroth.client.terrain.grasses.TallGrass;
import us.asciiroth.client.terrain.triggers.Trigger;
import us.asciiroth.client.terrain.triggers.TriggerIf;
import us.asciiroth.client.terrain.triggers.TriggerIfNot;
import us.asciiroth.client.terrain.triggers.TriggerOnce;
import us.asciiroth.client.terrain.triggers.TriggerOnceIf;
import us.asciiroth.client.terrain.triggers.TriggerOnceIfNot;
import us.asciiroth.client.terrain.triggers.TriggerOnceOnDrop;
import us.asciiroth.client.terrain.triggers.TriggerOnceOnPickup;

/**
 * A singleton that ensures our collection of piece instances, which are all immutable, 
 * are cached for reuse. Also manages the serialization and deserialization of pieces, 
 * since the serialized form of a <code>Piece</code>, also known as its key, is used 
 * to cache the pieces for lookup as well. Finally, the serializers themselves contain 
 * additional information that is used by the Map Editor to represent and manipulate 
 * piece types.
 * <p>
 * <strong>It is essential that pieces are created through the Registry; the game assumes
 * two pieces, of the same type and with the same state, will be the same object instance.</strong> 
 * It is also required that all piece instances be immutable for this reason. With the 
 * exception of the player (who is an agent on the board), and effects (which are not saved 
 * and so never serialized), all changes in the game are modeled by swapping piece instances, 
 * not by changing piece state. 
 *    
 */
public class Registry {

    /** The delimiter used to mark fields in serialized keys. The actual value can be used
     * as-is in a regular expression to split the key.
     */
    public static final String FIELD_DELIMITER = "\\|"; // regexp (S)plitter
    
    private final Map<String, Piece> cache = new HashMap<String, Piece>();
    private final Map<String, Serializer<? extends Piece>> sers = 
        new HashMap<String, Serializer<? extends Piece>>();
    
    private static Registry instance = new Registry();
    /**
     * Get the <code>Registry</code> singleton.
     * @return  the <code>Registry</code> singleton.
     */
    public static Registry get() {
        return instance;
    }
    
    /**
     * For the supplied key, create a piece instance. <strong>Pieces should be created using 
     * the registry exclusively.</strong> The game assumes that two pieces with the same type 
     * and the same state, being immutable, are the same object instance. For this method 
     * to work, an appropriate <code>Serializer</code> must be created for the piece type 
     * and registered with the registry. 
     *  
     * @param key       a String representation of the piece and its state     
     * @return          a piece instance
     */
    public Piece getPiece(String key) {
        Piece p = cache.get(key);
        if (p == null) {
            p = create(key);
            if (p != null) {
                cache.put(key, p);
            } else {
                throw new RuntimeException("Could not create: " + key);
            }
        }
        return p;
    }
    
    /**
     * For the supplied class, create a piece instance. <strong>Pieces should be created using
     * the Registry exclusively.</strong> The game assumes that two pieces with the same type 
     * and the same state, being immutable, are the same object instance. For this method 
     * to work, an appropriate <code>Serializer</code> must be created for the piece type 
     * and registered with the registry. 
     *  
     * @param c         the class of the piece to be created (must be a type-only serializable
     *                  piece, otherwise use the string key for the piece).      
     * @return          a piece instance
     */
    public Piece getPiece(Class<?> c) {
        return getPiece(Util.getType(c));
    }
    
    /**
     * For the supplied piece, get its "key", its representation or serialization 
     * in String form. This key can be used to re-create the piece instance, cache it, 
     * refer to it in a board, etc. For this method to work, an appropriate 
     * <code>Serializer</code> must be created for the piece type and registered with 
     * the registry. 
     *  
     * @param piece     the piece to create a key for     
     * @return          a String representation of the piece and its state
     */
    @SuppressWarnings("unchecked") // TODO
    public String getKey(Piece piece) {
        String type = Util.getType(piece);
        Serializer ser = sers.get(type);
        if (ser == null) {
            // One common occurrence of this exception is when a subclass has not 
            // implemented getTypeName(), you get a more abstract type that's 
            // never expected to be on the board like Decorator, that has no 
            // serializer registered.
            throw new RuntimeException("Could not find serializer for: " + type);
        }
        return ser.store(piece);
    }
    
    /**
     * Use the provided piece as an example to retrieve the cached version of this 
     * piece instance. Any time a piece is created, it should be passed to the registry
     * in this manner and the returned version should be used, in order to ensure that
     * equality holds between all piece instances (e.g. two Triffids with the color Red 
     * must be equals == to one another).   
     * @param piece
     * @return  the piece instance as it exists in the cache
     */
    @SuppressWarnings("unchecked")
    public <T extends Piece> T cache(T piece) {
        // TODO: Not actually correct
        return (T)getPiece( getKey(piece) );
    }
    
    /**
     * Get the templates for all available pieces; used by the editor to create a library of 
     * types from which individual instances can be created.
     * 
     * @return  a Map of keys and their associated templates
     */
    public Map<String, Map<String, String>> getSerializersByTags() {
        Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
        
        for (Map.Entry<String, Serializer<?>> entry : sers.entrySet()) {
            String key = entry.getKey();
            Serializer<?> s = entry.getValue();
            if (s.example().is(NOT_EDITABLE) || s.example() instanceof Effect) {
                    continue; 
            }
            Map<String, String> entries = map.get(s.getTag());
            if (entries == null) {
                entries = new HashMap<String, String>();
                map.put(s.getTag(), entries);
            }
            entries.put(key, s.template(key));
        }
        return map;
    }
    /**
     * Get an example instance for the given type. These are used by the editor to illustrate
     * the types, since some types can't be rendered without additional information.
     * 
     * @param type  the type of a piece
     * @return      an instance of that piece for illustration of the type in the editor
     */
    public Piece getExample(String type) {
        Serializer<?> s = sers.get(type);
        return s.example();
    }
    
    private Registry() {
        /* --------------------------------------------------------------------- */
        /* ITEMS */
        /* --------------------------------------------------------------------- */
        add(AmmoBow.class, AmmoBow.SERIALIZER);
        add(AmmoGun.class, AmmoGun.SERIALIZER);
        add(AmmoParalyzer.class, AmmoParalyzer.SERIALIZER);
        add(AmmoSling.class, AmmoSling.SERIALIZER);
        
        add(Agentray.class, Agentray.SERIALIZER);
        add(Apple.class, Apple.SERIALIZER);
        add(Arrow.class, Arrow.SERIALIZER);
        add(BlueRing.class, BlueRing.SERIALIZER);
        add(Bomb.class, Bomb.SERIALIZER);
        add(Bone.class, Bone.SERIALIZER);
        add(Bow.class, Bow.SERIALIZER);
        add(Bread.class, Bread.SERIALIZER);
        add(Bridge.class, Bridge.SERIALIZER);
        add(Bullet.class, Bullet.SERIALIZER);
        add(Chalice.class, Chalice.SERIALIZER);
        add(Chalk.class, Chalk.SERIALIZER);
        add(CopperPill.class, CopperPill.SERIALIZER);
        add(Crowbar.class, Crowbar.SERIALIZER);
        add(Crystal.class, Crystal.SERIALIZER);
        add(Dagger.class, Dagger.SERIALIZER);
        add(EnergyCloud.class, EnergyCloud.SERIALIZER);
        add(EuclideanShard.class, EuclideanShard.SERIALIZER);
        add(Fireball.class, Fireball.SERIALIZER);
        add(Fish.class, Fish.SERIALIZER);
        add(FishingPole.class, FishingPole.SERIALIZER);
        add(GoldCoin.class, GoldCoin.SERIALIZER);
        add(GoldenHarp.class, GoldenHarp.SERIALIZER);
        add(Grenade.class, Grenade.SERIALIZER);
        add(Gun.class, Gun.SERIALIZER);
        add(Hammer.class, Hammer.SERIALIZER);
        add(Head.class, Head.SERIALIZER);
        add(Healer.class, Healer.SERIALIZER);
        add(HelmOfTheAsciiroth.class, HelmOfTheAsciiroth.SERIALIZER);
        add(KelpSmoothie.class, KelpSmoothie.SERIALIZER);
        add(GlassEye.class, GlassEye.SERIALIZER);
        add(Key.class, Key.SERIALIZER);
        add(Kiwi.class, Kiwi.SERIALIZER);
        add(MirrorShield.class, MirrorShield.SERIALIZER);
        add(Mushroom.class, Mushroom.SERIALIZER);
        add(Parabullet.class, Parabullet.SERIALIZER);
        add(Paralyzer.class, Paralyzer.SERIALIZER);
        add(PeachElixir.class, PeachElixir.SERIALIZER);
        add(PoisonCloud.class, PoisonCloud.SERIALIZER);
        add(PoisonDart.class, PoisonDart.SERIALIZER);
        add(ProteinBar.class, ProteinBar.SERIALIZER);
        add(PurpleMushroom.class, PurpleMushroom.SERIALIZER);
        add(RedRing.class, RedRing.SERIALIZER);
        add(Rock.class, Rock.SERIALIZER);
        add(Scroll.class, Scroll.SERIALIZER);
        add(SilverAnkh.class, SilverAnkh.SERIALIZER);
        add(Sling.class, Sling.SERIALIZER);
        add(SlingRock.class, SlingRock.SERIALIZER);
        add(Stoneray.class, Stoneray.SERIALIZER);
        add(Sword.class, Sword.SERIALIZER);
        add(TerminusEst.class, TerminusEst.SERIALIZER);
        add(Weakray.class, Weakray.SERIALIZER);
        
        // Fish
        
        /* --------------------------------------------------------------------- */
        /* TERRAIN */
        /* --------------------------------------------------------------------- */
        add(Altar.class, Altar.SERIALIZER);
        add(AgentGate.class, AgentGate.SERIALIZER);
        add(Boards.class, Boards.SERIALIZER);
        add(BeeHive.class, BeeHive.SERIALIZER);
        add(Bookshelf.class, Bookshelf.SERIALIZER);
        add(BubblingLava.class, BubblingLava.SERIALIZER);
        add(Bushes.class, Bushes.SERIALIZER);
        add(Campfire.class, Campfire.SERIALIZER);
        add(CaveEntrance.class, CaveEntrance.SERIALIZER);
        add(ChalkedFloor.class, ChalkedFloor.SERIALIZER);
        add(Chest.class, Chest.SERIALIZER);
        add(Cliff.class, Cliff.SERIALIZER);
        add(Cloud.class, Cloud.SERIALIZER);
        add(ColorRelay.class, ColorRelay.SERIALIZER);
        add(Crate.class, Crate.SERIALIZER);
        add(Crevasse.class, Crevasse.SERIALIZER);
        add(Dirt.class, Dirt.SERIALIZER);
        add(Door.class, Door.SERIALIZER);
        add(DualTerrain.class, DualTerrain.SERIALIZER);
        add(EmptyChest.class, EmptyChest.SERIALIZER);
        add(EnergyCloud.class, EnergyCloud.SERIALIZER);
        add(Equipper.class, Equipper.SERIALIZER);
        add(EuclideanEngine.class, EuclideanEngine.SERIALIZER);
        add(EuclideanTransporter.class, EuclideanTransporter.SERIALIZER);
        add(Exchanger.class, Exchanger.SERIALIZER);
        add(FarthapodNest.class, FarthapodNest.SERIALIZER);
        add(Fence.class, Fence.SERIALIZER);
        add(Field.class, Field.SERIALIZER);
        add(FishPool.class, FishPool.SERIALIZER);
        add(Flagger.class, Flagger.SERIALIZER);
        add(Floor.class, Floor.SERIALIZER);
        add(Flowers.class, Flowers.SERIALIZER);
        add(Fountain.class, Fountain.SERIALIZER);
        add(ForceField.class, ForceField.SERIALIZER);
        add(Forest.class, Forest.SERIALIZER);
        add(Gate.class, Gate.SERIALIZER);
        add(Grass.class, Grass.SERIALIZER);
        add(Haystack.class, Haystack.SERIALIZER);
        add(HighRocks.class, HighRocks.SERIALIZER);
        add(ImpassableCliffs.class, ImpassableCliffs.SERIALIZER);
        add(KeySwitch.class, KeySwitch.SERIALIZER);
        add(Lava.class, Lava.SERIALIZER);
        add(LowRocks.class, LowRocks.SERIALIZER);
        add(Messenger.class, Messenger.SERIALIZER);
        add(Mud.class, Mud.SERIALIZER);
        add(Ocean.class, Ocean.SERIALIZER);
        add(Pier.class, Pier.SERIALIZER);
        add(Pit.class, Pit.SERIALIZER);
        add(PressurePlate.class, PressurePlate.SERIALIZER);
        add(Pylon.class, Pylon.SERIALIZER);
        add(PyramidWall.class, PyramidWall.SERIALIZER);
        add(Reflector.class, Reflector.SERIALIZER);
        add(Rubble.class, Rubble.SERIALIZER);
        add(RustyGate.class, RustyGate.SERIALIZER);
        add(Sand.class, Sand.SERIALIZER);
        add(Scrub.class, Scrub.SERIALIZER);
        add(ShallowSwamp.class, ShallowSwamp.SERIALIZER);
        add(ShallowWater.class, ShallowWater.SERIALIZER);
        add(Shooter.class, Shooter.SERIALIZER);
        add(Sign.class, Sign.SERIALIZER);
        add(Sky.class, Sky.SERIALIZER);
        add(StairsDown.class, StairsDown.SERIALIZER);
        add(StairsUp.class, StairsUp.SERIALIZER);
        add(Surf.class, Surf.SERIALIZER);
        add(Swamp.class, Swamp.SERIALIZER);
        add(Switch.class, Switch.SERIALIZER);
        add(Teleporter.class, Teleporter.SERIALIZER);
        add(TrashPile.class, TrashPile.SERIALIZER);
        add(PitTrap.class, PitTrap.SERIALIZER);
        add(EnergyTrapContainer.class, EnergyTrapContainer.SERIALIZER);
        add(PoisonTrapContainer.class, PoisonTrapContainer.SERIALIZER);
        add(Raft.class, Raft.SERIALIZER);
        add(ResistancesCloud.class, ResistancesCloud.SERIALIZER);
        add(ResistancesTrapContainer.class, ResistancesTrapContainer.SERIALIZER);
        add(Throne.class, Throne.SERIALIZER);
        add(Tumbleweed.class, Tumbleweed.SERIALIZER);
        add(Turnstile.class, Turnstile.SERIALIZER);
        add(Unequipper.class, Unequipper.SERIALIZER);
        add(Unflagger.class, Unflagger.SERIALIZER);
        add(Urn.class, Urn.SERIALIZER);
        add(VendingMachine.class, VendingMachine.SERIALIZER);
        add(Wall.class, Wall.SERIALIZER);
        add(Water.class, Water.SERIALIZER);
        add(Waterfall.class, Waterfall.SERIALIZER);
        add(Weeds.class, Weeds.SERIALIZER);
        add(WishingWell.class, WishingWell.SERIALIZER);
        add(WoodPiling.class, WoodPiling.SERIALIZER);
        
        /* --------------------------------------------------------------------- */
        /* AGENTS */
        /* --------------------------------------------------------------------- */
        add(Asciiroth.class, Asciiroth.SERIALIZER);
        add(Boulder.class, Boulder.SERIALIZER);
        add(Cephalid.class, Cephalid.SERIALIZER);
        add(Corvid.class, Corvid.SERIALIZER);
        add(Farthapod.class, Farthapod.SERIALIZER);
        add(GreatOldOne.class, GreatOldOne.SERIALIZER);
        add(Hooloovoo.class, Hooloovoo.SERIALIZER);
        add(KillerBee.class, KillerBee.SERIALIZER);
        add(LavaWorm.class, LavaWorm.SERIALIZER);
        add(LightningLizard.class, LightningLizard.SERIALIZER);
        add(Optilisk.class, Optilisk.SERIALIZER);
        add(Paralyzed.class, Paralyzed.SERIALIZER);
        add(Pillar.class, Pillar.SERIALIZER);
        add(Pusher.class, Pusher.SERIALIZER);
        add(Sleestak.class, Sleestak.SERIALIZER);
        add(Rhindle.class, Rhindle.SERIALIZER);
        add(RollingBoulder.class, RollingBoulder.SERIALIZER);
        add(Slider.class, Slider.SERIALIZER);
        add(Statue.class, Statue.SERIALIZER);
        add(Stump.class, Stump.SERIALIZER);
        add(Tetrite.class, Tetrite.SERIALIZER);
        add(Thermadon.class, Thermadon.SERIALIZER);
        add(Triffid.class, Triffid.SERIALIZER);
        // NPCs
        add(Archer.class, Archer.SERIALIZER);
        add(Commoner.class, Commoner.SERIALIZER);
        add(Noble.class, Noble.SERIALIZER);
        add(Rifleman.class, Rifleman.SERIALIZER);
        add(Wizard.class, Wizard.SERIALIZER);
        // Mallocs
        add(MallocArcher.class, MallocArcher.SERIALIZER);
        add(MallocCommoner.class, MallocCommoner.SERIALIZER);
        add(MallocNoble.class, MallocNoble.SERIALIZER);
        add(MallocRifleman.class, MallocRifleman.SERIALIZER);
        add(MallocWizard.class, MallocWizard.SERIALIZER);
        
        /* --------------------------------------------------------------------- */
        /* TREES */
        /* --------------------------------------------------------------------- */
        add(Alder.class, Alder.SERIALIZER);
        add(Cactus.class, Cactus.SERIALIZER);
        add(Cypress.class, Cypress.SERIALIZER);
        add(Elm.class, Elm.SERIALIZER);
        add(Fir.class, Fir.SERIALIZER);
        add(Hemlock.class, Hemlock.SERIALIZER);
        add(Maple.class, Maple.SERIALIZER);
        add(Oak.class, Oak.SERIALIZER);
        add(Spruce.class, Spruce.SERIALIZER);
        add(Willow.class, Willow.SERIALIZER);
        
        /* --------------------------------------------------------------------- */
        /* GRASSES */
        /* --------------------------------------------------------------------- */
        add(BeachGrass.class, BeachGrass.SERIALIZER);
        add(BunchGrass.class, BunchGrass.SERIALIZER);
        add(SwampGrass.class, SwampGrass.SERIALIZER);
        add(TallGrass.class, TallGrass.SERIALIZER);
        
        /* --------------------------------------------------------------------- */
        /* TRIGGERS */
        /* --------------------------------------------------------------------- */
        add(Trigger.class, Trigger.SERIALIZER);
        add(TriggerIf.class, TriggerIf.SERIALIZER);
        add(TriggerIfNot.class, TriggerIfNot.SERIALIZER);
        add(TriggerOnce.class, TriggerOnce.SERIALIZER);
        add(TriggerOnceIf.class, TriggerOnceIf.SERIALIZER);
        add(TriggerOnceIfNot.class, TriggerOnceIfNot.SERIALIZER);
        add(TriggerOnceOnDrop.class, TriggerOnceOnDrop.SERIALIZER);
        add(TriggerOnceOnPickup.class, TriggerOnceOnPickup.SERIALIZER);
        
        add(AgentDestroyer.class, AgentDestroyer.SERIALIZER);
        add(PieceCreator.class, PieceCreator.SERIALIZER);
        add(Timer.class, Timer.SERIALIZER);
        add(WinGame.class, WinGame.SERIALIZER);
        add(PlayerGate.class, PlayerGate.SERIALIZER);
        
        /* --------------------------------------------------------------------- */
        /* COMPOUND TERRAIN */
        /* --------------------------------------------------------------------- */
        add(Mimic.class, Mimic.SERIALIZER);
        add(SecretPassage.class, SecretPassage.SERIALIZER);

        add(Fire.class, Fire.SERIALIZER);
    }
    private void add(Class<?> c, Serializer<?> serializer) {
        sers.put(Util.getType(c), serializer);
    }
    
    private Piece create(String key) {
        try {
            String[] args = key.split(FIELD_DELIMITER);
            Serializer<?> serializer = sers.get(args[0]);
            if (serializer == null) {
                throw new RuntimeException("Deserializer cannot be found for key: " + args[0]);
            }
            return serializer.create(args);
        } catch(Throwable throwable) {
            throw new RuntimeException("Serializer.create() failed for: " + key);
        }
    }

    public String getEntitySpec() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table>");
        for (Map.Entry<String, Serializer<?>> entry : sers.entrySet()) {
            Serializer<?> s = entry.getValue();
            sb.append("<tr><td class='s'>");
            sb.append(s.example().getSymbol().getAdjustedEntity()).append("</td><td class='s'>");
            sb.append(s.example().getSymbol().getAdvanced()).append("</td><td>");
            sb.append(s.example().getName());
            sb.append("</td></tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }
}
