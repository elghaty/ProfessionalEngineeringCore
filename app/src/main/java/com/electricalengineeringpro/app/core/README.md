# Professional Engineering Core

ElectricalEngineeringPro uses ONE logical engineering calculation boundary:

ProfessionalEngineeringCore

The core is modular internally and contains specialized calculators.

Architecture:

UI
 ↓
ViewModel / Service
 ↓
ProfessionalEngineeringFacade
 ↓
ProfessionalEngineeringCore
 ↓
Modular Engineering Calculators

Modules:

- Power
- Load
- Load Schedule
- Network
- Cable
- Breaker
- Breaker Selection
- Voltage Drop
- Short Circuit
- Transformer
- Transformer Sizing
- Generator
- Motor
- Pump
- SLD

Rules:

1. Engineering formulas must not exist in Compose UI.
2. Engineering formulas must not be duplicated in ViewModels.
3. There must not be a second engineering core.
4. ProfessionalEngineeringCore is the single engineering boundary.
5. Internal active-power unit is kW.
6. Apparent power is kVA.
7. Current is A.
8. Short-circuit current is kA.
9. Engineering catalogs and standard-dependent values must eventually be versioned.
10. All critical calculators require automated tests.
