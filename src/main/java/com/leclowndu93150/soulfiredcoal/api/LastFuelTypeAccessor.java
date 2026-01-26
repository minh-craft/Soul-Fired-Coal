package com.leclowndu93150.soulfiredcoal.api;

import net.minecraft.world.inventory.ContainerData;

public interface LastFuelTypeAccessor {
    int soulfiredcoal$getLastFuelType();
    void soulfiredcoal$setLastFuelType(int type);

    default ContainerData soulfiredcoal$getWrappedDataAccess() {
        return null;
    }
}
