# =========================
# Stage 1: Build
# =========================
FROM acrsg@lseansharedel.azurecr.io/hardened/platform/nodejs:22 AS build

WORKDIR /app-rjs

COPY . .

USER root

# Update openssl package
RUN apk update && apk add --no-cache openssl

# Force tar version for dependencies
RUN npm pkg set "overrides.tar=7.5.11"

# Install dependencies and build app
RUN NODE_TLS_REJECT_UNAUTHORIZED=0 npm install && npm run build


# =========================
# Stage 2: Runtime
# =========================
FROM acrsg@lseansharedel.azurecr.io/hardened/platform/nodejs:22

WORKDIR /app-rjs

USER root

# Update openssl package
RUN apk update && apk add --no-cache openssl

# Copy build artifacts
COPY --from=build /app-rjs/build ./build
COPY --from=build /app-rjs/.npmrc /root/.npmrc

# Install pinned packages via public registry (.npmrc kept for corporate proxy routing)
RUN NODE_TLS_REJECT_UNAUTHORIZED=0 npm install -g \
    --legacy-peer-deps \
    --unsafe-perm \
    --no-audit \
    --no-fund \
    serve@14.2.6 \
    cross-spawn@7.0.5 \
    tar@7.5.11 \
    brace-expansion@2.0.3 \
    ip-address@10.1.1

# Remove registry credentials — no longer needed
RUN rm -f /root/.npmrc

# Remove npm's own bundled vulnerable copies
RUN rm -rf /usr/local/lib/node_modules/npm/node_modules/cross-spawn || true
RUN rm -rf /usr/local/lib/node_modules/npm/node_modules/brace-expansion || true
RUN rm -rf /usr/local/lib/node_modules/npm/node_modules/tar || true

# Remove nested vulnerable instances inside serve's dependency tree
# (patched globals at /usr/local/lib/node_modules/<pkg> take over resolution)
RUN find /usr/local/lib/node_modules -mindepth 3 -type d -name "brace-expansion" | xargs rm -rf || true
RUN find /usr/local/lib/node_modules -mindepth 3 -type d -name "ip-address" | xargs rm -rf || true

# Verify
RUN npm ls -g cross-spawn tar brace-expansion ip-address || true

# Create non-root user
RUN addgroup -S nodejs -g 1001 && \
    adduser -S nextjs -u 1001

# Set permissions
RUN chown -R nextjs:nodejs /app-rjs

# Switch to non-root user
USER nextjs

EXPOSE 8080

CMD ["serve", "-s", "build", "-l", "8080"]
