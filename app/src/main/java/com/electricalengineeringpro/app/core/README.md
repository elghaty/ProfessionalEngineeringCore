# ProfessionalEngineeringCore

This directory is the single engineering calculation boundary of the application.

The core is intentionally modular.

ProfessionalEngineeringCore.kt is the public facade.

Individual calculators are isolated so that each engineering discipline can be modified,
tested, validated, and extended independently without creating a monolithic calculation file.

UI layers must never contain engineering formulas.

All engineering calculations must enter through this core.
