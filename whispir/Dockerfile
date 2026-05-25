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

# Update npm
RUN NODE_TLS_REJECT_UNAUTHORIZED=0 npm install -g npm@latest serve

# Fix vulnerable cross-spawn
RUN NODE_TLS_REJECT_UNAUTHORIZED=0 npm install -g cross-spawn@7.0.5

# Fix vulnerable tar
RUN NODE_TLS_REJECT_UNAUTHORIZED=0 npm install -g tar@7.5.11

# Remove vulnerable brace-expansion bundled inside npm (not needed at runtime)
RUN rm -rf /usr/local/lib/node_modules/npm/node_modules/brace-expansion || true

# Verify installed versions
RUN npm ls -g cross-spawn || true
RUN npm ls -g tar || true
RUN npm --version

# Create non-root user
RUN addgroup -S nodejs -g 1001 && \
    adduser -S nextjs -u 1001

# Set permissions
RUN chown -R nextjs:nodejs /app-rjs

# Switch to non-root user
USER nextjs

EXPOSE 8080

CMD ["serve", "-s", "build", "-l", "8080"]
