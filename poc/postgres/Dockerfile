FROM postgres:15

# Copy initialization scripts
COPY init/ /docker-entrypoint-initdb.d/

# Expose the PostgreSQL port
EXPOSE 5432