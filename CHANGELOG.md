# Changelog

All notable changes to this project will be documented in this file.


---

## 0.0.3 - Hot Reload & Multi-Arg Support

### Added
- **Hot Reload**: Added a reload button (⟳) to reload plugins without restarting the application.
- **Multi-Argument Support**: Functions can now accept multiple arguments (e.g., `max(1, 2)`).
- **New Functions**: Added `max(a,b)` and `min(a,b)` as example multi-argument functions.
- **Robust Configuration**: Added fallback default configuration values if `application.yml` is missing.
- **Error Handling**: Improved error messages for invalid syntax and unknown functions.

### Fixed
- **SLF4J Warning**: Resolved "No SLF4J providers found" warning by adding `slf4j-simple` dependency.

### Changed
- **CalcFunction Interface**: Updated `evaluate(double)` to `execute(double... args)` to support variable arguments.
- **Performance**: Optimized expression parsing to clean whitespace only once.
- **FunctionRegistry**: Added duplicate function name detection.

---

## 0.0.2 - Visual Update+Common Configuration

### Added
- Modernized calculator UI (rounded buttons, spacing, new visuals).
- New unified visual theme across display, pager, and keypad sections.
- Added shared “common configuration” for button styles and layout spacing.
- Improved input/result display styling (card-style panel, cleaner typography).
- New pagination layout with better alignment and padding.
- Added "Inter" font as default calculator font styling.

### Improved
- Cleaner button rendering with hover effects.
- More consistent spacing across entire layout.
- Function buttons visually unified with basic buttons.

### Fixed
- Pagination arrows misalignment.
- Button background inconsistencies.
- Overly large paddings in top and bottom panels.

---

## 0.0.1 - Annotation Support

### Added
- Introduced annotation based module system using `@Function`.
- Developers can now create custom functions without implementing `CalcFunction`.
- Added automatic adapter to wrap annotated classes and expose them as functional modules.
- Added support for `run(double x)` as the standard method for annotated modules.
- Updated function discovery to load both interface based modules and annotation based modules.
- Create Changelog file

## Fixed
- Typo in License file

---