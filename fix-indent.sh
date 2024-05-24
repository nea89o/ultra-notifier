#!/usr/bin/env bash
# Sadly preprocessor likes to insert extra whitespace after a remap
# This removes the space after //$$ which should mean that remaps now
# are fully reversible by selecting the original project and running
# this script.

find src/main/ -type f -exec sed -i -e 's|//\$\$ |//$$|g' {} +
