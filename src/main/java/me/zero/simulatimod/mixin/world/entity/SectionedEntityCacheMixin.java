package me.zero.simulatimod.mixin.world.entity;

import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.util.function.LazyIterationConsumer;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.entity.EntityLike;
import net.minecraft.world.entity.EntityTrackingSection;
import net.minecraft.world.entity.EntityTrackingStatus;
import net.minecraft.world.entity.SectionedEntityCache;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

// TODO: Fix this causing entities to not be hittable
@Mixin(SectionedEntityCache.class)
public class SectionedEntityCacheMixin {
    private final Object2ObjectMap<ChunkSectionPos, EntityTrackingSection<?>> simulatiMod$trackingSections = new Object2ObjectOpenHashMap<>();
    private final ObjectSortedSet<ChunkSectionPos> simulatiMod$trackedPositions = new ObjectAVLTreeSet<>();
    @Shadow
    @Final
    private Class<? extends EntityLike> entityClass;
    private Object2ObjectFunction<ChunkPos, EntityTrackingStatus> simulatiMod$posToStatus;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void simulatiMod$constructorInjectSectionedEntityCache(Class<EntityLike> entityClass, Long2ObjectFunction<EntityTrackingStatus> chunkStatusDiscriminator, CallbackInfo ci) {
        this.simulatiMod$posToStatus = simulatiMod$longFunctionToObjectFunction(chunkStatusDiscriminator);
    }

    private Object2ObjectFunction<ChunkPos, EntityTrackingStatus> simulatiMod$longFunctionToObjectFunction(Long2ObjectFunction<EntityTrackingStatus> long2ObjectFunction) {
        return key -> {
            ChunkPos chunkPos = new ChunkPos((long) key);
            return long2ObjectFunction.apply(chunkPos.toLong());
        };
    }

    @Inject(method = "forEachInBox", at = @At("HEAD"), cancellable = true)
    public void simulatiMod$forEachInBoxCancellableInject(Box box, LazyIterationConsumer<EntityTrackingSection<?>> consumer, CallbackInfo ci) {
        ci.cancel();
        int minX = ChunkSectionPos.getSectionCoord(box.minX - 2.0);
        int minY = ChunkSectionPos.getSectionCoord(box.minY - 4.0);
        int minZ = ChunkSectionPos.getSectionCoord(box.minZ - 2.0);
        int maxX = ChunkSectionPos.getSectionCoord(box.maxX + 2.0);
        int maxY = ChunkSectionPos.getSectionCoord(box.maxY + 0.0);
        int maxZ = ChunkSectionPos.getSectionCoord(box.maxZ + 2.0);
        for (int x = minX; x <= maxX; ++x) {
            ChunkSectionPos from = ChunkSectionPos.from(x, 0, 0);
            ChunkSectionPos to = ChunkSectionPos.from(x, -1, -1);
            // r + 1L
            for (ChunkSectionPos chunkSection : this.simulatiMod$trackedPositions.subSet(to, from)) {
                int sectionY = chunkSection.getSectionY();
                int sectionZ = chunkSection.getSectionZ();
                EntityTrackingSection<?> entityTrackingSection = this.simulatiMod$trackingSections.get(chunkSection);
                if (sectionY < minY || sectionY > maxY || sectionZ < minZ || sectionZ > maxZ || (entityTrackingSection) == null || entityTrackingSection.isEmpty() || !entityTrackingSection.getStatus().shouldTrack() || !consumer.accept(entityTrackingSection).shouldAbort())
                    continue;
                return;
            }
        }
    }

    public Stream<ChunkSectionPos> simulatiMod$getSections(ChunkPos chunkPos) {
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;
        ObjectSortedSet<ChunkSectionPos> chunkSectionSortedSet = this.simulatiMod$getSections(chunkX, chunkZ);
        if (chunkSectionSortedSet.isEmpty()) {
            return Stream.empty();
        }
        ObjectBidirectionalIterator<ChunkSectionPos> chunkSectionIterator = chunkSectionSortedSet.iterator();
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(chunkSectionIterator, Spliterator.ORDERED | Spliterator.DISTINCT | Spliterator.SORTED | Spliterator.NONNULL | Spliterator.IMMUTABLE), false);
    }

    private ObjectSortedSet<ChunkSectionPos> simulatiMod$getSections(int chunkX, int chunkZ) {
        ChunkSectionPos from = ChunkSectionPos.from(chunkX, 0, chunkZ);
        ChunkSectionPos to = ChunkSectionPos.from(chunkX, -1, chunkZ);
        // to + 1L
        return this.simulatiMod$trackedPositions.subSet(from, to);
    }

    public void simulatiMod$removeSection(ChunkSectionPos sectionPos) {
        this.simulatiMod$trackingSections.remove(sectionPos);
        this.simulatiMod$trackedPositions.remove(sectionPos);
    }

    public ObjectSet<ChunkPos> simulatiMod$getChunkPositions() {
        ObjectOpenHashSet<ChunkPos> chunkPositions = new ObjectOpenHashSet<>();
        this.simulatiMod$trackingSections.keySet().forEach(sectionPos -> chunkPositions.add(new ChunkPos(sectionPos.getSectionX(), sectionPos.getSectionZ())));
        return chunkPositions;
    }

    public EntityTrackingSection<?> simulatiMod$getTrackingSection(ChunkSectionPos sectionPos) {
        return this.simulatiMod$trackingSections.computeIfAbsent(sectionPos, this::simulatiMod$addSection);
    }

    @Nullable
    public EntityTrackingSection<?> simulatiMod$findTrackingSection(ChunkSectionPos sectionPos) {
        return this.simulatiMod$trackingSections.get(sectionPos);
    }

    private EntityTrackingSection<? extends EntityLike> simulatiMod$addSection(ChunkSectionPos sectionPos) {
        ChunkPos statusPos = new ChunkPos(sectionPos.getSectionX(), sectionPos.getSectionZ());
        EntityTrackingStatus entityTrackingStatus = this.simulatiMod$posToStatus.get(statusPos);
        this.simulatiMod$trackedPositions.add(sectionPos);
        return new EntityTrackingSection<>(this.entityClass, entityTrackingStatus);
    }
}
