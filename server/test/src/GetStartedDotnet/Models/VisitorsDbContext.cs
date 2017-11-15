using Microsoft.EntityFrameworkCore;

namespace GetStartedDotnet.Models
{
    public class VisitorsDbContext : DbContext
    {
        public VisitorsDbContext(DbContextOptions options) : base(options)
        {
        }

        public DbSet<User> Users { get; set; }
        public DbSet<Task> Tasks { get; set; }
    }
}
