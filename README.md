# Eventure_

Eventure – Android Application

Overview

Eventure is an Android application that displays events on an interactive map. Users can explore nearby events, view detailed information for each event, and filter events based on time and category.

This project was developed as my graduation project, focusing on clean architecture, third-party API integration, and map-based UI interactions.

⸻

Features

    •   Interactive map using Google Maps API
    •   Custom map markers representing events
    •   Event detail view when selecting a marker
    •   Filter events by time
    •   Filter events by category
    •   User authentication using Firebase Authentication
    •   Location-based event visualization
	•	Clean architecture with clear separation of concerns

⸻

Tech Stack

	•	Language: Kotlin
	•	Architecture: MVVM (for user/authentication)
	•	UI: Android Views / XML
	•	Maps: Google Maps API
	•	Authentication: Firebase Authentication
	•	Data Handling: Repository pattern
	•	Async: Kotlin Coroutines
	•	Build Tool: Gradle

⸻

Architecture

The app separates responsibilities while keeping the architecture straightforward:

	•	MapFragment / Activities: Handles UI rendering and event filtering logic
	•	ViewModel: Manages user authentication state with Firebase
	•	Repository: Handles data sources and interactions with Firebase

This structure isolates authentication logic while keeping map UI interactions responsive and intuitive.

⸻

Authentication

Firebase Authentication is used to manage user sign-in.

	•	Supports email and phone-based authentication
	•	Authentication logic is isolated in the ViewModel
	•	Implemented for learning and demonstration purposes
	•	Simplified due to external service constraints (e.g., billing, SMS/email delivery)

⸻

Filtering Logic

Filtering is implemented directly in the MapFragment:

	•	Time filters — show upcoming or events within a selected time range
	•	Category filters — filter by event type

The map updates dynamically when filters are applied, giving users a responsive and interactive experience.

⸻

Key Challenges & Learnings

	•	Integrating and customizing Google Maps API
	•	Managing map markers and click interactions
	•	Implementing combined time and category filtering
	•	Working with Firebase Authentication and external service limitations
	•	Structuring a clean and maintainable Android project

⸻

Known Limitations

	•	Firebase Authentication is simplified for demonstration purposes
	•	External services (SMS/email) may not work fully due to configuration or billing limits
	•	The project is intended as a portfolio and learning project, not production-ready

⸻

Future Improvements

	•	Improve authentication flow and error handling
	•	Add marker clustering for large numbers of events
	•	Enhance UI/UX and animations
	•	Add unit tests for repository and user-related ViewModel



https://github.com/user-attachments/assets/c8cd6d9b-0c3a-45a2-a6d8-6c65ec7ad769


	
