import { Box, Button, FormControl, InputLabel, MenuItem, Select, TextField, Paper, Typography, Stack, Alert, CircularProgress } from "@mui/material"
import { useState } from "react"
import { addActivity } from "../services/api"
import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline"

type ActivityFormProps = {
  onActivityAdded: () => void
}

type ActivityFormData = {
  type: string
  duration: string
  caloriesBurned: string
  additionalMetrics: Record<string, unknown>
}

const ActivityForm = ({ onActivityAdded }: ActivityFormProps) => {
  const [activity, setActivity] = useState<ActivityFormData>({
    type: "RUNNING",
    duration: "",
    caloriesBurned: "",
    additionalMetrics: {}
  })
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [success, setSuccess] = useState(false)

  const handleSubmit = async (event: React.SyntheticEvent<HTMLFormElement>) => {
    event.preventDefault()
    setError(null)
    setSuccess(false)

    // Validation
    if (!activity.duration || Number(activity.duration) <= 0) {
      setError("Please enter a valid duration")
      return
    }

    if (!activity.caloriesBurned || Number(activity.caloriesBurned) <= 0) {
      setError("Please enter valid calories burned")
      return
    }

    setLoading(true)

    try {
      await addActivity({
        activityType: activity.type,
        durationMinutes: Number(activity.duration),
        caloriesBurned: Number(activity.caloriesBurned),
        startedAt: new Date().toISOString(),
        metrics: activity.additionalMetrics
      })

      setSuccess(true)
      setActivity({
        type: "RUNNING",
        duration: "",
        caloriesBurned: "",
        additionalMetrics: {}
      })

      // Clear success message after 3 seconds
      setTimeout(() => setSuccess(false), 3000)

      // Trigger parent callback after a short delay
      setTimeout(() => onActivityAdded(), 500)
    } catch (error) {
      console.error("Failed to add activity:", error)
      setError("Failed to add activity. Please try again.")
    } finally {
      setLoading(false)
    }
  }

  return (
    <Paper
      component="form"
      onSubmit={handleSubmit}
      sx={{
        p: 3,
        mb: 4,
        background: "linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)",
        borderRadius: 2,
      }}
    >
      <Box sx={{ display: "flex", alignItems: "center", gap: 1, mb: 3 }}>
        <AddCircleOutlineIcon sx={{ color: "primary.main", fontSize: 28 }} />
        <Typography variant="h6" sx={{ fontWeight: 600 }}>
          Log New Activity
        </Typography>
      </Box>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      {success && (
        <Alert severity="success" sx={{ mb: 2 }}>
          Activity added successfully!
        </Alert>
      )}

      <Stack spacing={2.5}>
        <FormControl fullWidth>
          <InputLabel>Activity Type</InputLabel>
          <Select
            value={activity.type}
            label="Activity Type"
            onChange={(event) =>
              setActivity({ ...activity, type: event.target.value as string })
            }
            disabled={loading}
          >
            <MenuItem value="RUNNING">🏃 Running</MenuItem>
            <MenuItem value="WALKING">🚶 Walking</MenuItem>
            <MenuItem value="CYCLING">🚴 Cycling</MenuItem>
            <MenuItem value="SWIMMING">🏊 Swimming</MenuItem>
            <MenuItem value="WEIGHT_TRAINING">💪 Weight Training</MenuItem>
            <MenuItem value="YOGA">🧘 Yoga</MenuItem>
          </Select>
        </FormControl>

        <TextField
          fullWidth
          label="Duration (Minutes)"
          type="number"
          placeholder="e.g., 30"
          inputProps={{ min: 1 }}
          value={activity.duration}
          onChange={(event) =>
            setActivity({ ...activity, duration: event.target.value })
          }
          disabled={loading}
          required
        />

        <TextField
          fullWidth
          label="Calories Burned"
          type="number"
          placeholder="e.g., 250"
          inputProps={{ min: 1 }}
          value={activity.caloriesBurned}
          onChange={(event) =>
            setActivity({ ...activity, caloriesBurned: event.target.value })
          }
          disabled={loading}
          required
        />

        <Button
          type="submit"
          variant="contained"
          size="large"
          fullWidth
          disabled={loading}
          sx={{ fontWeight: 600 }}
        >
          {loading ? <CircularProgress size={24} sx={{ mr: 1 }} /> : null}
          {loading ? "Adding..." : "Add Activity"}
        </Button>
      </Stack>
    </Paper>
  )
}

export default ActivityForm