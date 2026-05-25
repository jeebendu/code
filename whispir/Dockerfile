FROM acrsg01seanshared01.azurecr.io/hardened/platform/nodejs:22

WORKDIR /app-rjs

USER root

RUN apk update && apk add --no-cache openssl

# Copy dependency manifests first for layer cache efficiency
COPY package*.json .npmrc* ./

RUN NODE_TLS_REJECT_UNAUTHORIZED=0 npm install

COPY . .

RUN npm run build

# Remove node_modules — not needed to serve static files
RUN rm -rf node_modules

# Remove private registry config before global install — those packages are on public npm only
RUN rm -f /root/.npmrc

RUN NODE_TLS_REJECT_UNAUTHORIZED=0 npm install -g \
    --legacy-peer-deps \
    --unsafe-perm \
    --no-audit \
    --no-fund \
    cross-spawn@7.0.5 \
    tar@7.5.11 \
    brace-expansion@2.1.1 \
    serve@14.2.6

# Remove vulnerable versions bundled inside npm itself
RUN rm -rf /usr/local/lib/node_modules/npm/node_modules/cross-spawn || true
RUN rm -rf /usr/local/lib/node_modules/npm/node_modules/brace-expansion || true
RUN rm -rf /usr/local/lib/node_modules/npm/node_modules/tar || true

ENV PATH="/usr/local/bin:/usr/local/lib/node_modules/.bin:${PATH}"

RUN addgroup --system --gid 1001 nodejs
RUN adduser --system --uid 1001 nextjs

RUN chown -R nextjs:nodejs /app-rjs

USER nextjs

EXPOSE 8080

CMD ["/usr/local/bin/serve", "-s", "build", "-l", "8080"]
