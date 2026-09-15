# Professional Engineering Core

This is the single engineering calculation boundary.

The core is NOT one giant file.

It is a modular engineering system composed of independent calculators.

## Rules

- UI contains no engineering formulas.
- ViewModels contain no engineering formulas.
- Repositories contain no engineering formulas.
- Database contains no engineering formulas.
- Each engineering calculation has its own calculator.
- ProfessionalEngineeringCore is the single public engineering entry point.
- Every calculator must be independently testable.
- Engineering units are explicit.
- Power is internally represented in kW.
- Apparent power is represented in kVA.
- Current is represented in A.
- Voltage is represented in V.
- Short-circuit current is represented in kA.

## Future Modules

- GeneratorCalculator
- ProtectionCalculator
- CoordinationCalculator
- EarthingCalculator
- ArcFlashCalculator
- HarmonicsCalculator
- SolarCalculator
- BatteryCalculator

These modules must be added without creating a second engineering core.
