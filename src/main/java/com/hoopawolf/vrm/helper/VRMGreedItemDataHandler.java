package com.hoopawolf.vrm.helper;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hoopawolf.vrm.data.GreedItemData;
import com.hoopawolf.vrm.ref.Reference;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class VRMGreedItemDataHandler
{
    public static final VRMGreedItemDataHandler INSTANCE = new VRMGreedItemDataHandler();
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Identifier.class, new Identifier.Serializer())
            .create();
    private static final Type ITEM_DATA_TYPE = new TypeToken<GreedItemData>()
    {
    }.getType();

    public final ArrayList<GreedItemData> data = new ArrayList<>();
    public final ArrayList<String> blackList = new ArrayList<>();

    public void findFiles(ModContainer mod, String base, Predicate<Path> rootFilter,
                          BiFunction<Path, Path, Boolean> processor, boolean visitAllFiles)
    {
        if (mod.getMetadata().getId().equals("minecraft") || mod.getMetadata().getId().equals("forge"))
        {
            return;
        }

        try
        {
            Path root = mod.getRootPath().resolve(base);

            if (root == null || !Files.exists(root) || !rootFilter.test(root))
            {
                return;
            }

            if (processor != null)
            {
                Iterator<Path> itr = Files.walk(root).iterator();

                while (itr.hasNext())
                {
                    boolean cont = processor.apply(root, itr.next());

                    if (!visitAllFiles && !cont)
                    {
                        return;
                    }
                }
            }
        } catch (IOException ex)
        {
            throw new UncheckedIOException(ex);
        }
    }

    public void initJSON()
    {
        Collection<ModContainer> mods = FabricLoader.getInstance().getAllMods();
        Map<Pair<ModContainer, Identifier>, String> foundData = new HashMap<>();

        mods.forEach(mod ->
        {
            String id = mod.getMetadata().getId();
            findFiles(mod, String.format("data/%s/%s", id, Reference.VRM_DATA_LOCATION), (path) -> Files.exists(path),
                    (path, file) ->
                    {
                        if (file.toString().endsWith("greeditemblacklist.json"))
                        {
                            String fileStr = file.toString().replaceAll("\\\\", "/");
                            String relPath = fileStr
                                    .substring(fileStr.indexOf(Reference.VRM_DATA_LOCATION) + Reference.VRM_DATA_LOCATION.length() + 1);

                            String assetPath = fileStr.substring(fileStr.indexOf("data/"));
                            Identifier bookId = new Identifier(id, relPath);
                            foundData.put(Pair.of(mod, bookId), assetPath);
                        }

                        return true;
                    }, true);
        });

        foundData.forEach((pair, file) ->
        {
            ModContainer mod = pair.getLeft();
            Identifier res = pair.getRight();

            try (InputStream stream = Files.newInputStream(mod.getPath(file)))
            {
                saveData(stream);
            } catch (Exception e)
            {
                Reference.LOGGER.error("Failed to load greedblacklist {} defined by mod {}, skipping",
                        res, mod.getMetadata().getId(), e);
            }
        });

        for (GreedItemData dataList : data)
        {
            blackList.addAll(dataList.getListItems());
        }
    }

    public void saveData(InputStream stream)
    {
        Reader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        GreedItemData dataList = GSON.fromJson(reader, ITEM_DATA_TYPE);

        data.add(dataList);
    }
}
