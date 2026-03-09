import { Box, Typography, Divider, Paper } from '@mui/material';
import FitnessCenterIcon from '@mui/icons-material/FitnessCenter'

import ActivityForm from '../components/ActivityForm';
import ActivityList from '../components/ActivityList';

const ActivitiesPage = () => {
  const handleActivityAdded = () => {
    // Trigger a refresh of the activity list if needed
    // Could also use a state management solution like Redux
    window.location.reload()
  }

  return (
    <Box sx={{ pb: 4 }}>
      {/* Page Header */}
      <Paper sx={{ p: 3, mb: 4, background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)", color: "white" }}>
        <Box sx={{ display: "flex", alignItems: "center", gap: 1, mb: 1 }}>
          <FitnessCenterIcon sx={{ fontSize: 32 }} />
          <Typography variant="h4" sx={{ fontWeight: 700 }}>
            Your Activities
          </Typography>
        </Box>
        <Typography variant="body1" sx={{ opacity: 0.95 }}>
          Track and monitor all your fitness activities in one place
        </Typography>
      </Paper>

      {/* Activity Form */}
      <ActivityForm
        onActivityAdded={handleActivityAdded}
      />

      {/* Divider */}
      <Divider sx={{ my: 3 }} />

      {/* Activity List Header */}
      <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
        Recent Activities
      </Typography>

      {/* Activity List */}
      <ActivityList />
    </Box>
  )
}

export default ActivitiesPage