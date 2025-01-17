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

package net.frozenblock.lib.config.api.instance;

import com.mojang.datafixers.DataFixer;
import java.nio.file.Path;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.frozenblock.lib.FrozenLibConstants;
import net.frozenblock.lib.FrozenLibLogUtils;
import net.frozenblock.lib.config.api.registry.ConfigLoadEvent;
import net.frozenblock.lib.config.api.registry.ConfigSaveEvent;
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig;
import net.frozenblock.lib.config.impl.networking.client.ClientConfigSyncNetworking;
import net.frozenblock.lib.platform.api.PlatformHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Config<T> {
	private final String modId;
	private final Path path;
	private final boolean supportsModification;
	@Nullable
	private final DataFixer dataFixer;
	@Nullable
	private final Integer version;
	private final Class<T> configClass;
	private T configInstance;
	private final T defaultInstance;
	@Getter
	@Setter
	private boolean synced = false;

	protected Config(String modId, Class<T> configClass, Path path, boolean supportsModification, @Nullable DataFixer dataFixer, @Nullable Integer version) {
		this.modId = modId;
		this.path = path;
		this.supportsModification = supportsModification;
		this.configClass = configClass;
		try {
			this.defaultInstance = this.configInstance = configClass.getConstructor().newInstance();
		} catch (Exception e) {
			throw new IllegalStateException("No default constructor for default config instance.", e);
		}
		this.dataFixer = dataFixer;
		this.version = version;
	}

	@NotNull
	@Contract(pure = true)
	public static Path makePath(String modId, String extension) {
		return Path.of("./config/" + modId + "." + extension);
	}

	public String modId() {
		return this.modId;
	}

	public Path path() {
		return this.path;
	}

	public boolean supportsModification() {
		return this.supportsModification;
	}

	@Nullable
	public DataFixer dataFixer() {
		return this.dataFixer;
	}

	@Nullable
	public Integer version() {
		return this.version;
	}

	/**
	 * @return The current config instance with modifications if applicable.
	 */
	public T config() {
		if (this.supportsModification()) return ConfigModification.modifyConfig(this, this.instance(), false);
		return this.instance();
	}

	/**
	 * @return The current config instance with config sync modifications.
	 * <p>
	 * If the config instance doesn't support modification, config syncing will also not work.
	 * @since 1.5
	 */
	public T configWithSync() {
		if (!this.supportsSync()) {
			FrozenLibLogUtils.logWarning(
				String.format("Config %s from %s", this.configClass().getSimpleName(), this.modId()) + " does not support modification, returning unmodified instance.",
				FrozenLibConstants.UNSTABLE_LOGGING
			);
			return this.instance();
		}
		return ConfigModification.modifyConfig(this, this.instance(), true);
	}

	/**
	 * @return If the current config supports modification and does not have the {@link UnsyncableConfig} annotation.
	 * @since 1.5
	 */
	public boolean supportsSync() {
		return this.supportsModification() && !this.configClass().isAnnotationPresent(UnsyncableConfig.class);
	}

	/**
	 * @return The unmodified current config instance.
	 */
	public T instance() {
		return this.configInstance;
	}

	public void setConfig(T configInstance) {
		this.configInstance = configInstance;
	}

	public T defaultInstance() {
		return this.defaultInstance;
	}

	public Class<T> configClass() {
		return this.configClass;
	}

	public void onSync(T syncInstance) {}

	/**
	 * @since 1.5
	 */
	protected String formattedName() {
		return String.format("config %s from %s", this.configClass().getSimpleName(), this.modId());
	}

	protected abstract void onSave() throws Exception;

	protected abstract boolean onLoad() throws Exception;

	public final void save() {
		String formatted = this.formattedName();
        FrozenLibLogUtils.LOGGER.info("Saving {}", formatted);
		try {
			this.onSave();

			if (FrozenLibConstants.isInitialized) {
				if (PlatformHelper.envType() == EnvType.CLIENT)
					ClientConfigSyncNetworking.trySendC2S(this);

				invokeSaveEvents();
			}
		} catch (Exception e) {
			FrozenLibLogUtils.logError("Error while saving " + formatted, e);
		}
	}

	public final boolean load() {
		String formatted = this.formattedName();
		FrozenLibLogUtils.LOGGER.info("Loading {}", formatted);
		try {
			boolean loadVal = this.onLoad();

			if (FrozenLibConstants.isInitialized) {
				invokeLoadEvents();
			}
			return loadVal;
		} catch (Exception e) {
			FrozenLibLogUtils.logError("Error while loading " + formatted, e);
			return false;
		}
	}

	private void invokeSaveEvents() {
		String formatted = this.formattedName();
		try {
			ConfigSaveEvent.EVENT.invoke(e -> {
                try {
                    e.onSave(this);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });

			if (PlatformHelper.envType() == EnvType.CLIENT) {
				ConfigSaveEvent.Client.EVENT.invoke(e -> {
                    try {
                        e.onSave(this);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });
			}
		} catch (Exception e) {
			FrozenLibLogUtils.logError("Error in config save events for " + formatted, e);
		}
	}

	private void invokeLoadEvents() {
		String formatted = this.formattedName();
		try {
			ConfigLoadEvent.EVENT.invoke(e -> {
                try {
                    e.onLoad(this);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });

			if (PlatformHelper.envType() == EnvType.CLIENT) {
				ConfigLoadEvent.Client.EVENT.invoke(e -> {
                    try {
                        e.onLoad(this);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });
			}
		} catch (Exception e) {
			FrozenLibLogUtils.logError("Error in config load events for " + formatted, e);
		}
	}
}
