package net.frozenblock.lib.platform.api.fabric;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import static org.quiltmc.qsl.frozenblock.fabric.core.registry.api.sync.ModProtocol.LOGGER;
import static org.quiltmc.qsl.frozenblock.fabric.core.registry.api.sync.ModProtocol.add;
import org.quiltmc.qsl.frozenblock.fabric.core.registry.api.sync.ModProtocolDef;

public class ModProtocolHelperImpl {

	public static void loadVersions() {
		for (var container : FabricLoader.getInstance().getAllMods()) {
			var data = container.getMetadata();
			var frozenRegistry = data.getCustomValue("frozenlib_registry");

			if (frozenRegistry == null) {
				continue;
			}

			if (frozenRegistry.getType() != CustomValue.CvType.OBJECT) {
				LOGGER.warn("Mod {} ({}) contains invalid 'frozenlib_registry' entry! Expected 'OBJECT', found '{}'", container.getMetadata().getName(), container.getMetadata().getId(), frozenRegistry.getType());
				continue;
			}

			var value = frozenRegistry.getAsObject().get("mod_protocol");

			if (value == null || value.getType() == CustomValue.CvType.NULL) {
				continue;
			}

			if (value.getType() == CustomValue.CvType.OBJECT) {
				var object = value.getAsObject();

				var optional = false;
				var optVal = object.get("optional");

				if (optVal != null) {
					if (optVal.getType() != CustomValue.CvType.BOOLEAN) {
						invalidEntryType(".optional", container, CustomValue.CvType.BOOLEAN, optVal.getType());
						continue;
					}

					optional = optVal.getAsBoolean();
				}

				var version = decodeVersion(".value", container, object.get("value"));

				if (version != null) {
					add(new ModProtocolDef("mod:" + data.getId(), data.getName(), version, optional));
				}
			} else {
				var version = decodeVersion("", container, value);
				if (version != null) {
					add(new ModProtocolDef("mod:" + data.getId(), data.getName(), version, false));
				}
			}
		}
	}

	private static @Nullable IntList decodeVersion(String path, ModContainer container, CustomValue value) {
		if (value == null) {
			invalidEntryType(path, container, CustomValue.CvType.NUMBER, CustomValue.CvType.NULL);
			return null;
		} else if (value.getType() == CustomValue.CvType.NUMBER) {
			var i = value.getAsNumber().intValue();
			if (i < 0) {
				negativeEntry(path, container, i);
				return null;
			}

			return IntList.of(i);
		} else if (value.getType() == CustomValue.CvType.ARRAY) {
			var array = value.getAsArray();
			var versions = new IntArrayList(array.size());
			for (var i = 0; i < array.size(); i++) {
				var entry = array.get(i);
				if (entry.getType() == CustomValue.CvType.NUMBER) {
					var version = entry.getAsNumber().intValue();
					if (version < 0) {
						negativeEntry(path + "[" + i + "]", container, version);
						return null;
					}

					versions.add(version);
				} else {
					invalidEntryType(path + "[" + i + "]", container, CustomValue.CvType.NUMBER, entry.getType());
					return null;
				}
			}

			return versions;
		} else {
			invalidEntryType(path + ".optional", container, CustomValue.CvType.NUMBER, value.getType());
			return null;
		}
	}

	private static void invalidEntryType(String path, @NotNull ModContainer c, CustomValue.@NotNull CvType expected, CustomValue.@NotNull CvType found) {
		LOGGER.warn("Mod {} ({}) contains invalid 'frozenlib_registry.mod_protocol{}' entry! Expected '{}', found '{}'", path, c.getMetadata().getName(), c.getMetadata().getId(), expected.name(), found.name());
	}

	private static void negativeEntry(String path, @NotNull ModContainer c, int i) {
		LOGGER.warn("Mod {} ({}) contains invalid 'frozenlib_registry.mod_protocol{}' entry! Protocol requires non-negative integer, found '{}'!", path, c.getMetadata().getName(), c.getMetadata().getId(), i);
	}
}
