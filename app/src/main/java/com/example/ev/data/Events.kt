package com.example.ev.data

data class Events (var latitude: String, var longitude: String, var category: String, var type: String, var date: String, var startTime: String, var endTime: String, var entrancefee: String, var description: String) {
    override fun toString(): String {
        return "$category: $type\n" +
                "$date $startTime - $endTime\n" +
                "Entrance Fee: $entrancefee\n" +
                "$description"
    }
}