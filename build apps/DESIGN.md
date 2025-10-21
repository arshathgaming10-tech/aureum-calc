Glassmorphism Financial — Design Brief

Overview

A premium glassmorphism calculator targeted at professionals and design-conscious users. This file contains the concept, color palette, typography, layout notes, and an AI image prompt you can drop into Midjourney / DALL·E / Stable Diffusion.

Concept

Name: Glassmorphism Financial
Target audience: Professionals — finance, freelancers, and design-conscious general users.
Key differentiator: Layered glass cards, blurred background, elegant typography, and micro-animations for results and button presses. Focus on clarity for numeric data and a premium feel.

Color palette

- Primary gradient: #667EEA → #764BA2
- Accent (CTA): #00C2A8
- Primary text: #FFFFFF
- Secondary text: #DDE2FF
- Glass overlay tint: rgba(255,255,255,0.06)
- Error: #FF6B6B

Typography

- Display / Numbers: Poppins (SemiBold)
- UI labels / Buttons: Inter (Regular / Medium)

Layout & Components

- Top bar: small translucent bar with theme and history toggles.
- Display: Large glass card with expression (small) + current input/result (large, right-aligned).
- History: Slide-in glass tape with export (CSV) and copy-on-tap.
- Keypad: 4×N grid of rounded glass buttons; operation keys have accent color.
- Effects: Backdrop blur, thin white border (8–12% alpha), subtle inner shadow, press scale to 0.96 + ripple.

Image prompt (Midjourney / DALL·E style)

"A high-resolution UI screen for a mobile calculator app with a premium glassmorphism design; background is a soft blurred vertical gradient from #667EEA to #764BA2 with subtle bokeh; large translucent glass display card centered, slight blur and soft white border, showing '1234.56' in Poppins semi-bold; keypad below with semi-transparent glass buttons, rounded corners, subtle inner shadows and thin white border; operation keys accented in teal #00C2A8 with soft glow; minimal icons in top bar; overall mood: elegant, premium, modern; highly detailed, realistic lighting and reflections, crisp typography, 9:20 aspect ratio --ar 9:20 --v 5 --style raw"

Notes

- For Android implementation use Jetpack Compose with a blurred background via RenderEffect on Android 12+ or fallbacks for lower API levels.
- Use typography scaling and accessibility settings for large text.
