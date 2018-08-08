package com.futchas.volume

import org.springframework.stereotype.Service

@Service
class HistoricalVolumesService {

    fun isUnusualVolume(percentageDifference: Double) :Boolean {
        val unusualVolumePercentage = 30
        if(percentageDifference > unusualVolumePercentage)
            return true
//        else if(abs(percentageDifference) > unusualVolumePercentage)
//            return true

        return false
    }

    fun getVolumeDifference(globalVolumes1h: HistoricalVolumes, globalVolumes24h: HistoricalVolumes) : Double {
        val average24hVolume = globalVolumes24h.values.average()
        val average1hVolume = globalVolumes1h.values.sum()

        val volumeDifference = average1hVolume - average24hVolume

        return volumeDifference / average24hVolume * 100
    }


//    fun printVolumes(globalVolumes: HistoricalVolumes, period: TimePeriod) {
//        val percentageThreshold = 50
//
//        val volumeValues = globalVolumes.values
//        println("\nIn the last ${period.value}:")
//        val averageVolume = volumeValues.average()
//        println("Average volume $averageVolume")
//        val lastVolume = volumeValues.last()
//        println("Latest volume $lastVolume")
//
//        val volumeDifference = lastVolume - averageVolume
//        val percentageDifference = volumeDifference / averageVolume * 100
//        println("Volume diff $volumeDifference")
//        println("Percentage diff $percentageDifference")
//
//        when {
//            percentageDifference > percentageThreshold -> println("Volume changed more than $percentageThreshold%")
//            abs(percentageDifference) > percentageThreshold -> println("Volume changed to less than -$percentageThreshold%")
//            else -> println("Volume within usual range of $percentageThreshold%")
//        }
//    }


}