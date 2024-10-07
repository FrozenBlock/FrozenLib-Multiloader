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

package net.frozenblock.lib.tag.api;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class TagKeyArgument<T> implements ArgumentType<TagKeyArgument.Result<T>> {
	private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo:bar", "012", "#skeletons", "#minecraft:skeletons");
	final ResourceKey<? extends Registry<T>> registryKey;

	public TagKeyArgument(ResourceKey<? extends Registry<T>> registryKey) {
		this.registryKey = registryKey;
	}

	@Contract(value = "_ -> new", pure = true)
	public static <T> @NotNull TagKeyArgument<T> tagKey(ResourceKey<? extends Registry<T>> registryKey) {
		return new TagKeyArgument<>(registryKey);
	}

	public static <T> Result<T> getTagKey(
		@NotNull CommandContext<CommandSourceStack> context, String argument, ResourceKey<Registry<T>> registryKey, DynamicCommandExceptionType dynamicCommandExceptionType
	) throws CommandSyntaxException {
		Result<?> result = context.getArgument(argument, Result.class);
		Optional<Result<T>> optional = result.cast(registryKey);
		return optional.orElseThrow(() -> dynamicCommandExceptionType.create(result));
	}

	public Result<T> parse(@NotNull StringReader reader) throws CommandSyntaxException {
		int cursor = reader.getCursor();

		try {
			reader.skip();
			ResourceLocation resourceLocation = ResourceLocation.read(reader);
			return new Result<>(TagKey.create(this.registryKey, resourceLocation));
		} catch (CommandSyntaxException var4) {
			reader.setCursor(cursor);
			throw var4;
		}
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> commandContext, SuggestionsBuilder suggestionsBuilder) {
		Object var4 = commandContext.getSource();
		return var4 instanceof SharedSuggestionProvider sharedSuggestionProvider
			? sharedSuggestionProvider.suggestRegistryElements(this.registryKey, SharedSuggestionProvider.ElementSuggestionType.TAGS, suggestionsBuilder, commandContext)
			: suggestionsBuilder.buildFuture();
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}

	public static class Info<T> implements ArgumentTypeInfo<TagKeyArgument<T>, Info<T>.Template> {
		public void serializeToNetwork(@NotNull Template template, @NotNull FriendlyByteBuf buffer) {
			buffer.writeResourceLocation(template.registryKey.location());
		}

		public @NotNull Template deserializeFromNetwork(@NotNull FriendlyByteBuf buffer) {
			ResourceLocation resourceLocation = buffer.readResourceLocation();
			return new Template(ResourceKey.createRegistryKey(resourceLocation));
		}

		public void serializeToJson(@NotNull Template template, @NotNull JsonObject json) {
			json.addProperty("registry", template.registryKey.location().toString());
		}

		public @NotNull Template unpack(@NotNull TagKeyArgument<T> argument) {
			return new Template(argument.registryKey);
		}

		public final class Template implements ArgumentTypeInfo.Template<TagKeyArgument<T>> {
			final ResourceKey<? extends Registry<T>> registryKey;

			Template(ResourceKey<? extends Registry<T>> registryKey) {
				this.registryKey = registryKey;
			}

			public @NotNull TagKeyArgument<T> instantiate(@NotNull CommandBuildContext context) {
				return new TagKeyArgument<>(this.registryKey);
			}

			@Override
			public @NotNull ArgumentTypeInfo<TagKeyArgument<T>, ?> type() {
				return Info.this;
			}
		}
	}

	record Result<T>(TagKey<T> key) {
		public <E> Optional<Result<E>> cast(ResourceKey<? extends Registry<E>> registryKey) {
			return this.key.cast(registryKey).map(Result::new);
		}

		@Contract(pure = true)
		public @NotNull String asPrintable() {
			return "#" + this.key.location();
		}
	}
}
