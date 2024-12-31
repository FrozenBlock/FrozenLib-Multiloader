/*
 * Copyright (C) 2024 FrozenBlock
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.frozenblock.lib.gravity.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.frozenblock.lib.event.api.FrozenEvent;
import net.frozenblock.lib.event.entrypoint.CommonEventEntrypoint;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class GravityAPI {
    public static final double DEFAULT_GRAVITY = 1D;
    public static final FrozenEvent<GravityModification> MODIFICATIONS = FrozenEvent.createEvent(GravityModification.class);
    private static final Map<ResourceKey<Level>, List<GravityBelt<?>>> GRAVITY_BELTS = new HashMap<>();

    public static void register(ResourceKey<Level> dimension, GravityBelt<?> gravityBelt) {
		getAllBelts(dimension).add(gravityBelt);
    }

	@NotNull
    public static List<GravityBelt<?>> getAllBelts(ResourceKey<Level> dimension) {
        return GRAVITY_BELTS.computeIfAbsent(dimension, dimension1 -> new ArrayList<>());
    }

    public static @NotNull List<GravityBelt<?>> getAllBelts(@NotNull Level level) {
        return getAllBelts(level.dimension());
    }

    static {
        MODIFICATIONS.register(context -> {
            if (GRAVITY_BELTS.containsKey(context.dimension)) {
                Optional<GravityBelt<?>> optionalGravityBelt = getAffectingGravityBelt(GRAVITY_BELTS.get(context.dimension), context.y);
                if (optionalGravityBelt.isPresent()) {
                    GravityBelt<?> belt = optionalGravityBelt.get();
                    context.gravity = belt.getGravity(null, context.y);
                }
            }
        });
    }

    public static double calculateGravity(@NotNull Entity entity) {
        ResourceKey<Level> dimension = entity.level().dimension();
        double y = entity.getY();
        GravityContext context = new GravityContext(dimension, y, entity, entity.getInBlockState());
        MODIFICATIONS.invoke(e->e.modifyGravity(context));
        return context.gravity;
    }

    public static Optional<GravityBelt<?>> getAffectingGravityBelt(@NotNull List<GravityBelt<?>> belts, double y) {
        Optional<GravityBelt<?>> optionalGravityBelt = Optional.empty();
        for (GravityBelt<?> belt : belts) {
            if (belt.affectsPosition(y)) {
                optionalGravityBelt = Optional.of(belt);
                break;
            }
        }
        return optionalGravityBelt;
    }

    @FunctionalInterface
    public interface GravityModification extends CommonEventEntrypoint {
        void modifyGravity(GravityContext context);
    }
}
